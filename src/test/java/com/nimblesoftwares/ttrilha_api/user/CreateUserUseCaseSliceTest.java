package com.nimblesoftwares.ttrilha_api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.CreateUserRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.CreateUserResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.service.CreateUserService;
import com.nimblesoftwares.ttrilha_api.application.user.command.CreateUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class CreateUserUseCaseSliceTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  private Tracer tracer;

  @MockitoBean
  UserIdentityRepositoryPort userIdentityRepository;

  @MockitoBean
  CreateUserService createUserService;

  private final ProviderEnum PROVIDER = ProviderEnum.GOOGLE;
  private final String GOOGLE_ID = "12332103213912031321";

  @Test
  @DisplayName("happy path - should return new user id when user does not exist yet")
  public void test_shouldReturnNewUserIdWhenUserDoesNotExistYet() throws Exception {
    // Arrange
    UUID userId = UUID.randomUUID();
    CreateUserRequest request = createRequest();

    when(createUserService.execute(any(CreateUserCommand.class)))
        .thenReturn(userId);

    // Act
    MockHttpServletResponse response = mockMvc.perform(post("/api/v1/users")
            .with(jwt().jwt(jwt -> jwt
                .claim("sub", request.sub())
            ))
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andReturn().getResponse();

    // Assert
    URI location = URI.create(Objects.requireNonNull(response.getHeader("Location")));
    String locationUserId = location.getPath().substring(location.getPath().lastIndexOf("/") + 1);
    UUID locationUserIdUUID = UUID.fromString(locationUserId);

    CreateUserResponse responseBody = objectMapper.readValue(response.getContentAsString(), CreateUserResponse.class);

    assertEquals(userId, locationUserIdUUID);
    assertEquals(userId, UUID.fromString(responseBody.userId()));

    ArgumentCaptor<CreateUserCommand> captor =
        ArgumentCaptor.forClass(CreateUserCommand.class);

    verify(createUserService).execute(captor.capture());

    CreateUserCommand captured = captor.getValue();

    assertEquals("test@gmail.com", captured.email());
    assertEquals("my full name", captured.displayName());
    assertEquals("first name", captured.firstName());
    assertEquals("last name", captured.lastName());
    assertEquals("", captured.avatarUrl());
    assertEquals(PROVIDER, captured.provider());
    assertEquals(GOOGLE_ID, captured.providerUserId());
  }

  @Test
  @DisplayName("edge case - should return exception when request data is invalid")
  public void test_shouldReturnExceptionWhenRequestDataIsInvalid() throws Exception {
    // Arrange
    CreateUserRequest request = createInvalidRequest();

    // Act
    mockMvc.perform(post("/api/v1/users")
            .with(jwt().jwt(jwt -> jwt
                .claim("sub", request.sub())
            ))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(header().doesNotExist("Location"));

    // Assert
    verifyNoInteractions(createUserService);
  }

  @Test
  @DisplayName("edge case - should return user id when user alread exists")
  public void test_shouldReturnUserIdWhenUserAlreadyExists() throws Exception {
    // Arrange
    UUID userId = UUID.randomUUID();
    CreateUserRequest request = createRequest();
    when(createUserService.execute(any(CreateUserCommand.class))).thenReturn(userId);

    // Act and Assert
    mockMvc.perform(post("/api/v1/users")
            .with(jwt().jwt(jwt -> jwt
                .claim("sub", request.sub())
            ))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.userId").value(userId.toString()))
    ;
  }

  private CreateUserRequest createRequest() {
    return new CreateUserRequest(
        "test@gmail.com",
        "my full name",
        "first name",
        "last name",
        "",
        "google-oauth2|12332103213912031321"
    );
  }
  private CreateUserRequest createInvalidRequest() {
    return new CreateUserRequest(
        "@invalid email.com",
        "my full name",
        "first name",
        "last name",
        "",
        "google-oauth2|12332103213912031321"
    );
  }

}
