package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionSummaryResult;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListUserExpeditionsUseCase {
  List<ExpeditionSummaryResult> execute(UUID userId, Optional<ExpeditionStatus> status, String sort);
}
