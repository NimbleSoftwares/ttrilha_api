package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.RespondExpeditionInviteCommand;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.RespondExpeditionInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionForbiddenException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionInviteNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionMember;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

public class RespondExpeditionInviteService implements RespondExpeditionInviteUseCase {

  private final ExpeditionRepositoryPort expeditionRepository;

  public RespondExpeditionInviteService(ExpeditionRepositoryPort expeditionRepository) {
    this.expeditionRepository = expeditionRepository;
  }

  @Override
  public void execute(RespondExpeditionInviteCommand command) {
    Expedition expedition = expeditionRepository.findByInviteId(command.inviteId())
        .orElseThrow(() -> new ExpeditionInviteNotFoundException("Invite not found: " + command.inviteId()));

    ExpeditionMember invite = expedition.getMembers().stream()
        .filter(m -> m.getInviteId().equals(command.inviteId()))
        .findFirst()
        .orElseThrow(() -> new ExpeditionInviteNotFoundException("Invite not found: " + command.inviteId()));

    // Only the invitee can respond
    if (!invite.getUserId().equals(command.responderId())) {
      throw new ExpeditionForbiddenException("You can only respond to your own invites");
    }

    if (invite.getStatus() != MemberStatus.PENDING) {
      throw new ExpeditionForbiddenException("Invite is no longer pending");
    }

    if (command.response() != MemberStatus.ACCEPTED && command.response() != MemberStatus.REJECTED) {
      throw new ExpeditionForbiddenException("Response must be ACCEPTED or REJECTED");
    }

    expeditionRepository.updateMemberStatus(command.inviteId(), command.response());
  }
}

