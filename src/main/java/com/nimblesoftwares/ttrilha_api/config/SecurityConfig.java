package com.nimblesoftwares.ttrilha_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      return http
          .csrf(CsrfConfigurer::disable)
          .authorizeHttpRequests(authorize -> authorize
              .requestMatchers("/api/v1/auth/**").permitAll()
              .requestMatchers("/actuator/**").permitAll()
              .anyRequest().authenticated())
          .build();
  }
}
