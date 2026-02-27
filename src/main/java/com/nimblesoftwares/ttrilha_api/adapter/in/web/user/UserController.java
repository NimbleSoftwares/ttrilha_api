package com.nimblesoftwares.ttrilha_api.adapter.in.web.user;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper.UserMapper;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SaveUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserMapper userMapper;
  private final SaveUserUseCase saveUserUseCase;

  public UserController(
      UserMapper userMapper,
      SaveUserUseCase saveUserUseCase
  ) {
    this.userMapper = userMapper;
    this.saveUserUseCase = saveUserUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  @RequestMapping("/sync")
  public ResponseEntity<SaveUserResponse> sync(@AuthenticationPrincipal Jwt jwt) {

    SaveUserCommand command = userMapper.saveUserCommandfromJwt(jwt);
    UUID userId = saveUserUseCase.execute(command);
    SaveUserResponse response = new SaveUserResponse(userId.toString());

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(userId)
        .toUri();

    return ResponseEntity.created(location).body(response);
  }
}
