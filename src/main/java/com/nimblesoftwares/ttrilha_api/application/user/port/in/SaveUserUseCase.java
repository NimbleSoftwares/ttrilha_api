package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;

import java.util.UUID;

public interface SaveUserUseCase {

  UUID execute(SaveUserCommand command);
}
