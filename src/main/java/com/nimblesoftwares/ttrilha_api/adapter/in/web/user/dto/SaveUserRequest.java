package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SaveUserRequest(
    @NotBlank String displayName,
    @Email String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull String picture,
    @Pattern(
        regexp = "^[a-zA-Z0-9][a-zA-Z0-9_\\-]{2,29}$",
        message = "username must be 3-30 characters and contain only letters, numbers, underscores or hyphens"
    ) String username
){
  public SaveUserCommand toCommand(String subject) {
    ProviderIdentity providerIdentity = ProviderIdentity.fromSub(subject);
    return new SaveUserCommand(email, displayName, firstName, lastName, picture, username, providerIdentity.provider(), providerIdentity.providerUserId());
  }
}
