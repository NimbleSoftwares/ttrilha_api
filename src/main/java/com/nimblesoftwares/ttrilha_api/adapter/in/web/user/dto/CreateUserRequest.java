package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto;

import com.nimblesoftwares.ttrilha_api.application.user.command.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(
    @Email String email,
    @NotBlank String displayName,
    @NotBlank String firstName,
    @NotBlank String lastName,
    String avatarUrl
) {
  public CreateUserCommand toCommand() {
    return new CreateUserCommand(email, displayName, firstName, lastName, avatarUrl);
  }
}
