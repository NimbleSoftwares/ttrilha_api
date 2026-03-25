package com.nimblesoftwares.ttrilha_api.application.expedition.port.out;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpeditionRepositoryPort {
  UUID save(Expedition expedition);
  Optional<Expedition> findById(UUID id);
  List<Expedition> findAllByMemberWithAcceptedStatus(UUID userId);
  List<Expedition> findAllByMemberWithAcceptedStatusAndExpeditionStatus(UUID userId, ExpeditionStatus status);
  void update(Expedition expedition);
  void addMember(UUID expeditionId, UUID userId, MemberStatus status);
  void updateMemberStatus(UUID inviteId, MemberStatus status);
  int countInvitesByExpeditionAndInvitee(UUID expeditionId, UUID inviteeId);
  Optional<Expedition> findByInviteId(UUID inviteId);
  void removeOwnerAndCancel(UUID expeditionId, UUID ownerId);
  List<Expedition> findAllByPendingMember(UUID userId);
}

