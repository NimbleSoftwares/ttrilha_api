package com.nimblesoftwares.ttrilha_api.application.expedition.port.in;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionPendingInviteResult;

import java.util.List;
import java.util.UUID;

public interface ListPendingExpeditionInvitesUseCase {
  List<ExpeditionPendingInviteResult> execute(UUID userId);
}

