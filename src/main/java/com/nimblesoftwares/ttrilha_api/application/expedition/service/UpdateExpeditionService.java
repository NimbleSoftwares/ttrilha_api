package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.UpdateExpeditionCommand;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.UpdateExpeditionUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.command.SaveTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionForbiddenException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

public class UpdateExpeditionService implements UpdateExpeditionUseCase {

  private final ExpeditionRepositoryPort expeditionRepository;
  private final SaveTrailUseCase saveTrailUseCase;

  public UpdateExpeditionService(ExpeditionRepositoryPort expeditionRepository,
                                  SaveTrailUseCase saveTrailUseCase) {
    this.expeditionRepository = expeditionRepository;
    this.saveTrailUseCase = saveTrailUseCase;
  }

  @Override
  public void execute(UpdateExpeditionCommand command) {
    Expedition expedition = expeditionRepository.findById(command.expeditionId())
        .orElseThrow(() -> new ExpeditionNotFoundException("Expedition not found: " + command.expeditionId()));

    if (!expedition.getCreatedByUserId().equals(command.currentUserId())) {
      throw new ExpeditionForbiddenException("Only the expedition owner can edit it");
    }

    // Update scalar fields if provided
    if (command.title() != null) expedition.setTitle(command.title());
    if (command.startDate() != null) expedition.setStartDate(command.startDate());
    if (command.endDate() != null) expedition.setEndDate(command.endDate());

    // Trail change: if a new osmId is provided and it differs, save (idempotent) and update reference
    if (command.osmId() != null && !command.osmId().equals(expedition.getOsmId())) {
      saveTrailUseCase.execute(new SaveTrailCommand(
          command.nameTrail(),
          command.osmId(),
          command.tags(),
          command.geometry()
      ));
      expedition.setOsmId(command.osmId());
    }

    expeditionRepository.update(expedition);

    // Mark removed members
    if (command.removedMemberIds() != null) {
      for (var member : expedition.getMembers()) {
        if (command.removedMemberIds().contains(member.getUserId())
            && member.getStatus() != MemberStatus.REMOVED) {
          expeditionRepository.updateMemberStatus(member.getInviteId(), MemberStatus.REMOVED);
        }
      }
    }
  }
}

