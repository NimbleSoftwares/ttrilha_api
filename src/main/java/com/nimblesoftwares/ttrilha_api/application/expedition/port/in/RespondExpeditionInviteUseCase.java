package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.RespondExpeditionInviteCommand;

public interface RespondExpeditionInviteUseCase {
  void execute(RespondExpeditionInviteCommand command);
}

