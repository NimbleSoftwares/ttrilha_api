package com.nimblesoftwares.ttrilha_api.application.user.command;

import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

public record CreateUserCommand(
    String email,
    String displayName,
    String firstName,
    String lastName,
    String avatarUrl
    ) {
  public User toUser() {
    return new User(email, displayName, firstName, lastName, avatarUrl);
  }
}
