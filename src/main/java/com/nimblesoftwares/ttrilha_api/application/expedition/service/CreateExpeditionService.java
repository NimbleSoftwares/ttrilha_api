package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.CreateExpeditionCommand;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.CreateExpeditionUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.command.SaveTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionMember;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateExpeditionService implements CreateExpeditionUseCase {

  private final SaveTrailUseCase saveTrailUseCase;
  private final ExpeditionRepositoryPort expeditionRepository;

  public CreateExpeditionService(SaveTrailUseCase saveTrailUseCase,
                                  ExpeditionRepositoryPort expeditionRepository) {
    this.saveTrailUseCase = saveTrailUseCase;
    this.expeditionRepository = expeditionRepository;
  }

  @Override
  public UUID execute(CreateExpeditionCommand command) {
    // Ensure trail is persisted (idempotent — returns existing UUID if already saved)
    saveTrailUseCase.execute(new SaveTrailCommand(
        command.nameTrail(),
        command.osmId(),
        command.tags(),
        command.difficulty(),
        command.geometry()
    ));

    // Creator joins as ACCEPTED, crew as PENDING
    List<ExpeditionMember> members = new ArrayList<>();
    members.add(new ExpeditionMember(UUID.randomUUID(), command.createdByUserId(), MemberStatus.ACCEPTED));
    for (UUID crewId : command.crewIds()) {
      if (!crewId.equals(command.createdByUserId())) {
        members.add(new ExpeditionMember(UUID.randomUUID(), crewId, MemberStatus.PENDING));
      }
    }

    Expedition expedition = new Expedition();
    expedition.setTitle(command.title());
    expedition.setOsmId(command.osmId());
    expedition.setStartDate(command.startDate());
    expedition.setEndDate(command.endDate());
    expedition.setStatus(ExpeditionStatus.PLANNED);
    expedition.setCreatedByUserId(command.createdByUserId());
    expedition.setMembers(members);

    return expeditionRepository.save(expedition);
  }
}
