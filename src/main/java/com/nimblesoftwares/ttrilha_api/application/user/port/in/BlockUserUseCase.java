package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.command.BlockUserCommand;

public interface BlockUserUseCase {

  void execute(BlockUserCommand command);
}

