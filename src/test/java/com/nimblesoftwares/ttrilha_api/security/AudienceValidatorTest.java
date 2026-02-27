package com.nimblesoftwares.ttrilha_api.security;

import com.nimblesoftwares.ttrilha_api.config.AudienceValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceValidatorTest {

  @Test
  @DisplayName("happy path - Should validate audience")
  void test_shouldSucceedWhenAudienceIsCorrect() {
    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("aud", List.of("correct"))
        .build();

    AudienceValidator validator = new AudienceValidator("correct");

    assertFalse(validator.validate(jwt).hasErrors());
  }

  @Test
  @DisplayName("edge case - Should validate audience")
  void test_shouldFailWhenAudienceIsWrong() {
    Jwt jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .claim("aud", List.of("wrong"))
        .build();

    AudienceValidator validator = new AudienceValidator("correct");

    assertTrue(validator.validate(jwt).hasErrors());
  }
}