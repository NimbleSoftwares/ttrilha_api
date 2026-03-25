package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.UpdateExpeditionCommand;

public interface UpdateExpeditionUseCase {
  void execute(UpdateExpeditionCommand command);
}

