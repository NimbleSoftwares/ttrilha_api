package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.command.UnblockUserCommand;

public interface UnblockUserUseCase {

  void execute(UnblockUserCommand command);
}

