package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.InviteToExpeditionCommand;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.InviteToExpeditionUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionForbiddenException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionInviteConflictException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

public class InviteToExpeditionService implements InviteToExpeditionUseCase {

  private static final int MAX_INVITES = 3;

  private final ExpeditionRepositoryPort expeditionRepository;
  private final FriendshipRepositoryPort friendshipRepository;

  public InviteToExpeditionService(ExpeditionRepositoryPort expeditionRepository,
                                    FriendshipRepositoryPort friendshipRepository) {
    this.expeditionRepository = expeditionRepository;
    this.friendshipRepository = friendshipRepository;
  }

  @Override
  public void execute(InviteToExpeditionCommand command) {
    Expedition expedition = expeditionRepository.findById(command.expeditionId())
        .orElseThrow(() -> new ExpeditionNotFoundException("Expedition not found: " + command.expeditionId()));

    // Inviter must be an ACCEPTED member
    boolean inviterIsAccepted = expedition.getMembers().stream()
        .anyMatch(m -> m.getUserId().equals(command.inviterId()) && m.getStatus() == MemberStatus.ACCEPTED);
    if (!inviterIsAccepted) {
      throw new ExpeditionForbiddenException("Only expedition members can send invites");
    }

    // Invitee must be a friend of the inviter
    boolean areFriends = friendshipRepository.findByUserId(command.inviterId()).stream()
        .anyMatch(f -> f.getFriendId().equals(command.inviteeId()));
    if (!areFriends) {
      throw new ExpeditionForbiddenException("You can only invite friends to an expedition");
    }

    // Check if already an ACCEPTED or active PENDING member
    boolean alreadyActive = expedition.getMembers().stream()
        .anyMatch(m -> m.getUserId().equals(command.inviteeId())
            && (m.getStatus() == MemberStatus.ACCEPTED || m.getStatus() == MemberStatus.PENDING));
    if (alreadyActive) {
      throw new ExpeditionInviteConflictException("User is already a member or has a pending invite");
    }

    // Enforce max 3 invites per (expedition, invitee) across all-time rows
    int count = expeditionRepository.countInvitesByExpeditionAndInvitee(command.expeditionId(), command.inviteeId());
    if (count >= MAX_INVITES) {
      throw new ExpeditionInviteConflictException("Maximum invite attempts reached for this user");
    }

    expeditionRepository.addMember(command.expeditionId(), command.inviteeId(), MemberStatus.PENDING);
  }
}

