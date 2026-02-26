package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.command.CreateUserCommand;

import java.util.UUID;

public interface CreateUserUseCase {

  public UUID execute(CreateUserCommand command);
}
