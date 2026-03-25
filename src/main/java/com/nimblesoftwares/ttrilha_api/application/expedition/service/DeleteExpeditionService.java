package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.DeleteExpeditionUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionForbiddenException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;

import java.util.UUID;

public class DeleteExpeditionService implements DeleteExpeditionUseCase {

  private final ExpeditionRepositoryPort expeditionRepository;

  public DeleteExpeditionService(ExpeditionRepositoryPort expeditionRepository) {
    this.expeditionRepository = expeditionRepository;
  }

  @Override
  public void execute(UUID expeditionId, UUID ownerId) {
    Expedition expedition = expeditionRepository.findById(expeditionId)
        .orElseThrow(() -> new ExpeditionNotFoundException("Expedition not found: " + expeditionId));

    if (!expedition.getCreatedByUserId().equals(ownerId)) {
      throw new ExpeditionForbiddenException("Only the expedition owner can delete it");
    }

    // Soft-delete: owner removed from members + status set to CANCELLED
    expeditionRepository.removeOwnerAndCancel(expeditionId, ownerId);
  }
}

