package com.nimblesoftwares.ttrilha_api.user;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.service.SaveUserService;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.exception.UserPersistenceException;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentityId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserUseCase (Service Impl) Test")
class SaveUserUseCaseTest {

  @Mock
  private UserRepositoryPort userRepository;

  @Mock
  private UserIdentityRepositoryPort userIdentityRepository;

  @InjectMocks
  private SaveUserService createUserService;

  private final String GOOGLE_ID = "12332103213912031321";
  private final ProviderEnum PROVIDER = ProviderEnum.GOOGLE;

  @Test
  @DisplayName("happy path - should return new user id when user does not exist yet")
  void test_shouldReturnNewUserIdWhenUserDoesNotExistYet() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    SaveUserCommand command = createCommand();

    User savedUser = new User();
    savedUser.setId(expectedId);
    UserIdentity savedIdentity = mock(UserIdentity.class);

    when(userIdentityRepository.findByProviderAndProviderUserId(
        PROVIDER, GOOGLE_ID))
        .thenReturn(Optional.empty());

    when(userRepository.save(any(User.class)))
        .thenReturn(savedUser);

    when(userIdentityRepository.save(any(UserIdentity.class)))
        .thenReturn(savedIdentity);

    // Act
    UUID result = createUserService.execute(command);

    // Assert
    assertEquals(expectedId, result);

    verify(userIdentityRepository).findByProviderAndProviderUserId(
        PROVIDER, GOOGLE_ID);
    verify(userRepository).save(any(User.class));
    verify(userIdentityRepository).save(any(UserIdentity.class));
  }

  @Test
  @DisplayName("edge case - should return existing user id when identity already exists")
  void test_shouldReturnExistingUserIdWhenIdentityAlreadyExists() {
    // Arrange
    UUID existingUserId = UUID.randomUUID();
    SaveUserCommand command = createCommand();

    User existingUser = new User();
    existingUser.setId(existingUserId);

    UserIdentity existingIdentity = createUserIdentity(existingUser);

    when(userIdentityRepository.findByProviderAndProviderUserId(
        PROVIDER, GOOGLE_ID))
        .thenReturn(Optional.of(existingIdentity));

    when(userRepository.save(any(User.class)))
        .thenReturn(existingUser);

    // Act
    UUID result = createUserService.execute(command);

    // Assert
    assertEquals(existingUserId, result);

    verify(userRepository, times(1)).save(any());
    verify(userIdentityRepository, never()).save(any());
  }

  @Test
  @DisplayName("edge case - should never save user identity when fail to save the user")
  void test_shouldReturnUserPersistenceExceptionWhenFailToSaveTheUser() {
    // Arrange
    SaveUserCommand command = createCommand();
    when(userRepository.save(any()))
        .thenThrow(new UserPersistenceException("error"));

    // Act and Assert
    assertThrows(UserPersistenceException.class, () -> createUserService.execute(command));
    verify(userIdentityRepository, never()).save(any());
  }

  private SaveUserCommand createCommand() {
    return new SaveUserCommand(
        "email_example@gmail.com",
        "DisplayName",
        "FirstName",
        "LastName",
        null,
        PROVIDER,
        GOOGLE_ID
    );
  }

  private UserIdentity createUserIdentity(User user) {
    UserIdentityId userIdentityId = new UserIdentityId();
    userIdentityId.setUserId(user.getId());
    userIdentityId.setProvider(PROVIDER);
    userIdentityId.setProviderUserId(GOOGLE_ID);

    return new UserIdentity(
        userIdentityId,
        user
    );
  }
}