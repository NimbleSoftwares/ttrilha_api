package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import java.util.UUID;

public interface DeleteExpeditionUseCase {
  void execute(UUID expeditionId, UUID ownerId);
}

