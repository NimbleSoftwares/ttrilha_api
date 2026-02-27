package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveUserRequest(
    @NotBlank String displayName,
    @Email String email,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotNull String picture
){
  public SaveUserCommand toCommand(String subject) {
    ProviderIdentity providerIdentity = ProviderIdentity.fromSub(subject);
    return new SaveUserCommand(email, displayName, firstName, lastName, picture, providerIdentity.provider(), providerIdentity.providerUserId());
  }
}
