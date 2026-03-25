package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionDetailResult;

import java.util.UUID;

public interface GetExpeditionUseCase {
  ExpeditionDetailResult execute(UUID expeditionId, UUID currentUserId);
}

