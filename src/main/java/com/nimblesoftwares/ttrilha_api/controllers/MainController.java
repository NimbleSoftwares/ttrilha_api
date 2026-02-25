package com.nimblesoftwares.ttrilha_api.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MainController {

  @GetMapping("/test-jwt")
  public String testJwt(@AuthenticationPrincipal Jwt jwt) {
    return "JWT test successful, subject: " + jwt.getSubject();
  }
}
