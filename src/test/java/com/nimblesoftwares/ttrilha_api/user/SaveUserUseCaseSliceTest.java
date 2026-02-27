package com.nimblesoftwares.ttrilha_api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.UserController;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper.UserMapper;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.service.SaveUserService;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.exception.InvalidJwtClaimsException;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;
import com.nimblesoftwares.ttrilha_api.utils.JwtTestUtils;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class SaveUserUseCaseSliceTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  private Tracer tracer;

  @MockitoBean
  UserIdentityRepositoryPort userIdentityRepository;

  @MockitoBean
  UserMapper userMapper;

  @MockitoBean
  SaveUserService createUserService;

  private final String BASE_USERS_URL = "/api/v1/users";
  private final ProviderEnum PROVIDER = ProviderEnum.GOOGLE;
  private final String GOOGLE_ID = "12332103213912031321";
  private static final String SUB = "google-oauth2|12332103213912031321";

  @Test
  @DisplayName("happy path - should return new user id when user does not exist yet")
  public void test_shouldReturnNewUserIdWhenUserDoesNotExistYet() throws Exception {
    // Arrange
    UUID userId = UUID.randomUUID();
    SaveUserCommand saveUserCommand = createSaveUserCommand();

    when(userMapper.saveUserCommandfromJwt(any(Jwt.class)))
        .thenReturn(saveUserCommand);

    when(createUserService.execute(any(SaveUserCommand.class)))
        .thenReturn(userId);

    // Act
    MockHttpServletResponse response = mockMvc.perform(post(BASE_USERS_URL + "/sync")
            .with(jwt().jwt(jwt -> jwt
                .claims(c -> c.putAll(JwtTestUtils.createJwtClaimsSet(saveUserCommand).getClaims()))
            ))
          .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andReturn().getResponse();

    // Assert
    verify(userMapper).saveUserCommandfromJwt(any(Jwt.class));
    verify(createUserService).execute(any(SaveUserCommand.class));

    URI location = URI.create(Objects.requireNonNull(response.getHeader("Location")));
    String locationUserId = location.getPath().substring(location.getPath().lastIndexOf("/") + 1);
    UUID locationUserIdUUID = UUID.fromString(locationUserId);

    SaveUserResponse responseBody = objectMapper.readValue(response.getContentAsString(), SaveUserResponse.class);

    assertEquals(userId, locationUserIdUUID);
    assertEquals(userId, UUID.fromString(responseBody.userId()));

    ArgumentCaptor<SaveUserCommand> captor =
        ArgumentCaptor.forClass(SaveUserCommand.class);

    verify(createUserService).execute(captor.capture());

    SaveUserCommand captured = captor.getValue();

    assertEquals(saveUserCommand.email(), captured.email());
    assertEquals(saveUserCommand.displayName(), captured.displayName());
    assertEquals(saveUserCommand.firstName(), captured.firstName());
    assertEquals(saveUserCommand.lastName(), captured.lastName());
    assertEquals(saveUserCommand.avatarUrl(), captured.avatarUrl());
    assertEquals(saveUserCommand.provider(), captured.provider());
    assertEquals(saveUserCommand.providerUserId(), captured.providerUserId());
  }

  @Test
  @DisplayName("edge case - should return exception when request data is invalid")
  public void test_shouldReturnExceptionWhenRequestDataIsInvalid() throws Exception {

    // Arrange
    when(userMapper.saveUserCommandfromJwt(any(Jwt.class)))
        .thenThrow(new InvalidJwtClaimsException("Invalid jwt claims"));

    // Act and Assert
    mockMvc.perform(post(BASE_USERS_URL + "/sync")
            .with(jwt().jwt(jwt -> jwt
                .claim("sub", SUB)
                .claim("name", null)
            ))
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(header().doesNotExist("Location"));

    verifyNoInteractions(createUserService);
  }

  public SaveUserCommand createSaveUserCommand() {
    return new SaveUserCommand(
        "test@gmail.com",
        "my full name",
        "first name",
        "last name",
        "",
        PROVIDER,
        GOOGLE_ID
    );
  }
}
