package com.nimblesoftwares.ttrilha_api.config;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.CreateUserIfNotExistsFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
  private String issuerUri;

  @Value("${auth0.audience}")
  private String audience;

  @Value("${cors.allowed-origins:*}")
  private List<String> allowedOrigins;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      CreateUserIfNotExistsFilter createUserIfNotExistsFilter
  ) throws Exception {

      return http
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .csrf(CsrfConfigurer::disable)
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authorizeHttpRequests(authorize -> authorize
              .requestMatchers("/actuator/**").permitAll()
              .requestMatchers("/swagger-ui.html").permitAll()
              .requestMatchers("/swagger-ui/**").permitAll()
              .requestMatchers("/v3/api-docs/**").permitAll()
              .requestMatchers("/api-docs/**").permitAll()
              .requestMatchers("/webjars/**").permitAll()
              .requestMatchers("/api/v1/trails/**").permitAll()
              .anyRequest().authenticated())
          .oauth2ResourceServer(oauth2 -> oauth2.jwt(
              Customizer.withDefaults()))
          .addFilterAfter(createUserIfNotExistsFilter, BearerTokenAuthenticationFilter.class)
          .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(allowedOrigins);
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    config.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  @Profile("!test")
  public JwtDecoder jwtDecoder() {
    NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);

    OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
    OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

    jwtDecoder.setJwtValidator(withAudience);

    return jwtDecoder;
  }
}
