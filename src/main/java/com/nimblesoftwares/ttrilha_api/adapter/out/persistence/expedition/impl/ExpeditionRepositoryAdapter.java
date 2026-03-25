package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.entities.ExpeditionEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.interfaces.ExpeditionJpaRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.mapper.ExpeditionMapper;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ExpeditionRepositoryAdapter implements ExpeditionRepositoryPort {

  private final ExpeditionJpaRepository jpaRepository;
  private final ExpeditionMapper mapper;

  public ExpeditionRepositoryAdapter(ExpeditionJpaRepository jpaRepository,
                                      ExpeditionMapper mapper) {
    this.jpaRepository = jpaRepository;
    this.mapper = mapper;
  }

  @Override
  public UUID save(Expedition expedition) {
    ExpeditionEntity saved = jpaRepository.save(mapper.toPersistence(expedition));
    return saved.getId();
  }

  @Override
  public Optional<Expedition> findById(UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  public List<Expedition> findAllByMemberWithAcceptedStatus(UUID userId) {
    return jpaRepository.findAllByMemberWithAcceptedStatus(userId)
        .stream().map(mapper::toDomain).toList();
  }

  @Override
  public List<Expedition> findAllByMemberWithAcceptedStatusAndExpeditionStatus(UUID userId, ExpeditionStatus status) {
    return jpaRepository.findAllByMemberWithAcceptedStatusAndExpeditionStatus(userId, status)
        .stream().map(mapper::toDomain).toList();
  }

  @Override
  @Transactional
  public void update(Expedition expedition) {
    ExpeditionEntity entity = jpaRepository.findById(expedition.getId())
        .orElseThrow(() -> new IllegalArgumentException("Expedition not found: " + expedition.getId()));
    entity.setTitle(expedition.getTitle());
    entity.setOsmId(expedition.getOsmId());
    entity.setStartDate(expedition.getStartDate());
    entity.setEndDate(expedition.getEndDate());
    entity.setStatus(expedition.getStatus());
    jpaRepository.save(entity);
  }

  @Override
  @Transactional
  public void addMember(UUID expeditionId, UUID userId, MemberStatus status) {
    jpaRepository.addMember(expeditionId, userId, status);
  }

  @Override
  @Transactional
  public void updateMemberStatus(UUID inviteId, MemberStatus status) {
    jpaRepository.updateMemberStatus(inviteId, status);
  }

  @Override
  public int countInvitesByExpeditionAndInvitee(UUID expeditionId, UUID inviteeId) {
    return jpaRepository.countInvitesByExpeditionAndInvitee(expeditionId, inviteeId);
  }

  @Override
  public Optional<Expedition> findByInviteId(UUID inviteId) {
    return jpaRepository.findByInviteId(inviteId).map(mapper::toDomain);
  }

  @Override
  @Transactional
  public void removeOwnerAndCancel(UUID expeditionId, UUID ownerId) {
    jpaRepository.removeOwnerFromMembers(expeditionId, ownerId);
    jpaRepository.cancelExpedition(expeditionId);
  }

  @Override
  public List<Expedition> findAllByPendingMember(UUID userId) {
    return jpaRepository.findAllByPendingMember(userId)
        .stream().map(mapper::toDomain).toList();
  }
}
