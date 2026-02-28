package com.nimblesoftwares.ttrilha_api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserResponse;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import com.nimblesoftwares.ttrilha_api.security.TestSecurityConfig;
import com.nimblesoftwares.ttrilha_api.utils.JwtTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class SaveUserUseCaseIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepositoryPort userRepositoryPort;

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16");

  @DynamicPropertySource
  static void configureDatasource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  private final String BASE_USER_URL = "/api/v1/users";

  @Test
  @DisplayName("happy path - should return user id when jwt audience is the real one")
  void test_shouldReturnUserIdWhenAudienceIsCorrect() throws Exception {
    // Arrange
    String token = JwtTestUtils.generateJwtWithAudience("correct-audience");
    SaveUserRequest request = TestUserBuilders.createSaveUserRequest(false);

    // Act & Assert
    MockHttpServletResponse mvcResponse = mockMvc.perform(post(BASE_USER_URL + "/sync")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").isNotEmpty())
        .andExpect(header().exists("Location"))
        .andReturn().getResponse();

    SaveUserResponse response = objectMapper.readValue(mvcResponse
        .getContentAsString(), SaveUserResponse.class);

    Optional<User> user =userRepositoryPort.findById(UUID.fromString(response.userId()));

    assertTrue(user.isPresent());
    assertNotNull(user.get().getId());
    assertEquals(user.get().getDisplayName(), request.displayName());
  }

  @Test
  @DisplayName("edge case - should return 401 when jwt audience is not the real one")
  void test_shouldReturn401WhenAudienceIsInvalid() throws Exception {
    // Arrange
    String token = JwtTestUtils.generateJwtWithAudience("wrong-audience");
    SaveUserRequest request = TestUserBuilders.createSaveUserRequest(false);

    // Act & Assert
    mockMvc.perform(post(BASE_USER_URL + "/sync")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(header().doesNotExist("Location"));
  }
}