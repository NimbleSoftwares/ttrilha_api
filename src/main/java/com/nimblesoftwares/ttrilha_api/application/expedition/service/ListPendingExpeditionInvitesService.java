package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionPendingInviteResult;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.ListPendingExpeditionInvitesUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionMember;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListPendingExpeditionInvitesService implements ListPendingExpeditionInvitesUseCase {

  private final ExpeditionRepositoryPort expeditionRepository;
  private final UserRepositoryPort userRepository;

  public ListPendingExpeditionInvitesService(ExpeditionRepositoryPort expeditionRepository,
                                              UserRepositoryPort userRepository) {
    this.expeditionRepository = expeditionRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<ExpeditionPendingInviteResult> execute(UUID userId) {
    List<Expedition> expeditions = expeditionRepository.findAllByPendingMember(userId);

    List<ExpeditionPendingInviteResult> results = new ArrayList<>();
    for (Expedition exp : expeditions) {
      // Find the specific PENDING row for this user
      exp.getMembers().stream()
          .filter(m -> m.getUserId().equals(userId) && m.getStatus() == MemberStatus.PENDING)
          .findFirst()
          .ifPresent(invite -> {
            // Find the creator (they sent the initial invite or the only ACCEPTED member is the inviter)
            UUID inviterId = exp.getCreatedByUserId();
            User inviter = userRepository.findById(inviterId).orElse(null);
            results.add(new ExpeditionPendingInviteResult(
                invite.getInviteId(),
                exp.getId(),
                exp.getTitle(),
                inviterId,
                inviter != null ? inviter.getDisplayName() : "Unknown",
                inviter != null ? inviter.getAvatarUrl() : null
            ));
          });
    }
    return results;
  }
}

