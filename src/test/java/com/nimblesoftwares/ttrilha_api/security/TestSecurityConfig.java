package com.nimblesoftwares.ttrilha_api.security;

import com.nimblesoftwares.ttrilha_api.config.AudienceValidator;
import com.nimblesoftwares.ttrilha_api.utils.JwtTestUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.security.interfaces.RSAPublicKey;

@TestConfiguration
public class TestSecurityConfig {

  @Bean("testJwtDecoder")
  public JwtDecoder jwtDecoder() {

    NimbusJwtDecoder decoder = NimbusJwtDecoder
        .withPublicKey((RSAPublicKey) JwtTestUtils.getKeyPair().getPublic())
        .build();

    OAuth2TokenValidator<Jwt> withIssuer =
        JwtValidators.createDefaultWithIssuer("http://localhost:8080/");

    OAuth2TokenValidator<Jwt> audienceValidator =
        new AudienceValidator("correct-audience");

    decoder.setJwtValidator(
        new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator)
    );

    return decoder;
  }
}