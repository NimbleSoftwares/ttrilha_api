package com.nimblesoftwares.ttrilha_api.application.user.command;

import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentityId;

public record SaveUserCommand(
    String email,
    String displayName,
    String firstName,
    String lastName,
    String avatarUrl,
    String username,
    ProviderEnum provider,
    String providerUserId
    ) {

  public User toUser() {
    return new User(this.email(), this.displayName(), this.firstName(), this.lastName(), this.avatarUrl(), this.username());
  }

  public UserIdentity toUserIdentity(User user) {
    UserIdentityId userIdentityId = new UserIdentityId(user.getId(), this.provider(), this.providerUserId());
    return new UserIdentity(userIdentityId, user);
  }
}
