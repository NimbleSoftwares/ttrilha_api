package com.nimblesoftwares.ttrilha_api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.CreateUserRequest;
import com.nimblesoftwares.ttrilha_api.security.TestSecurityConfig;
import com.nimblesoftwares.ttrilha_api.utils.JwtTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CreateUserUseCaseIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Container
  static PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16");

  @Test
  @DisplayName("edge case - should return 401 when jwt audience is not the real one")
  void shouldReturn401WhenAudienceIsInvalid() throws Exception {
    // Arrange
    CreateUserRequest request = createRequest();
    String token = JwtTestUtils.generateJwtWithAudience("wrong-audience");

    // Act & Assert
    mockMvc.perform(post("/api/v1/users")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
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

}