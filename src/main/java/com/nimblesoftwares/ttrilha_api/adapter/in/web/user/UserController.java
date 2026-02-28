package com.nimblesoftwares.ttrilha_api.adapter.in.web.user;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserResponse;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SaveUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "User", description = "User Operations")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final SaveUserUseCase saveUserUseCase;

  public UserController(SaveUserUseCase saveUserUseCase) {
    this.saveUserUseCase = saveUserUseCase;
  }

  @PostMapping(value = "/sync", consumes = "application/json", produces = "application/json")
  @Operation(
      summary = "Sync or create user",
      description = "Creates a new user or updates authenticated user data based on the provided JWT. Extracts provider and provider user ID from JWT sub claim",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "201",
          description = "User successfully created or synced",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = SaveUserResponse.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Bad Request - Invalid input data"),
      @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or expired JWT"),
      @ApiResponse(responseCode = "403", description = "Forbidden - JWT audience or issuer mismatch"),
      @ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
  public ResponseEntity<SaveUserResponse> sync(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid SaveUserRequest request) {

    SaveUserCommand command = request.toCommand(jwt.getSubject());
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
