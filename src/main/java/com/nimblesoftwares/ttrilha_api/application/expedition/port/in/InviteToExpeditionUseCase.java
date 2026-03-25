package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.InviteToExpeditionCommand;

public interface InviteToExpeditionUseCase {
  void execute(InviteToExpeditionCommand command);
}

