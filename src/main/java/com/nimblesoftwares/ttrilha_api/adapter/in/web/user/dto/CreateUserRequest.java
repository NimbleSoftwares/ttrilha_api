package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.application.user.command.CreateUserCommand;
import com.nimbusds.jose.shaded.gson.annotations.SerializedName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

    @Email String email,

    @SerializedName("name")
    @NotBlank String displayName,

    @SerializedName("given_name")
    @NotBlank String firstName,

    @SerializedName("family_name")
    @NotBlank String lastName,

    @SerializedName("picture")
    String avatarUrl,

    @SerializedName("sub")
    @NotBlank String sub
) {

  public CreateUserCommand toCommand() {

    ProviderIdentity identity = ProviderIdentity.fromSub(sub);

    return new CreateUserCommand(email, displayName, firstName, lastName, avatarUrl, identity.provider(), identity.providerUserId());
  }
}
