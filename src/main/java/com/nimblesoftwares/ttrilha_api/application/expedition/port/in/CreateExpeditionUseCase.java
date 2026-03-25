package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.CreateExpeditionCommand;

import java.util.UUID;

public interface CreateExpeditionUseCase {
  UUID execute(CreateExpeditionCommand command);
}

