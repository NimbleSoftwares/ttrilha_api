package com.nimblesoftwares.ttrilha_api.adapter.in.web.user;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserRequest;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserResponse;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.UserSearchResponse;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserByUsernameUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SaveUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Tag(name = "User", description = "User Operations")
@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

  private final SaveUserUseCase saveUserUseCase;
  private final GetUserByUsernameUseCase getUserByUsernameUseCase;

  public UserController(SaveUserUseCase saveUserUseCase, GetUserByUsernameUseCase getUserByUsernameUseCase) {
    this.saveUserUseCase = saveUserUseCase;
    this.getUserByUsernameUseCase = getUserByUsernameUseCase;
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

  @GetMapping(value = "/search", produces = "application/json")
  @Operation(
      summary = "Search user by username",
      description = "Returns the public profile of a user matching the given username. Used to look up a user before sending a friendship invite.",
      security = @SecurityRequirement(name = "bearerAuth")
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "User found",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = UserSearchResponse.class)
          )
      ),
      @ApiResponse(responseCode = "400", description = "Bad Request - Invalid username format"),
      @ApiResponse(responseCode = "401", description = "Unauthorized"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<UserSearchResponse> searchByUsername(
      @RequestParam
      @Pattern(
          regexp = "^[a-zA-Z0-9][a-zA-Z0-9_\\-]{2,29}$",
          message = "username must be 3-30 characters and contain only letters, numbers, underscores or hyphens"
      ) String username) {

    var user = getUserByUsernameUseCase.execute(username);
    return ResponseEntity.ok(UserSearchResponse.fromDomain(user));
  }
}
