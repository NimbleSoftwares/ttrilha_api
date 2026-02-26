package com.nimblesoftwares.ttrilha_api.adapter.in.web.user;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.CreateUserRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.CreateUserResponse;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.CreateUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final CreateUserUseCase createUserUseCase;

  public UserController(
       CreateUserUseCase createUserUseCase
  ) {
    this.createUserUseCase = createUserUseCase;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<CreateUserResponse> create(@Valid @RequestBody CreateUserRequest request) {

    UUID createdId = createUserUseCase.execute(request.toCommand());
    CreateUserResponse response = new CreateUserResponse(createdId.toString());

    URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(createdId)
        .toUri();

    return ResponseEntity.created(location).body(response);
  }
}
