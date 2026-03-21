package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities.FriendshipSolicitationEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.interfaces.FriendshipSolicitationJpaRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.mapper.FriendshipSolicitationMapper;
import com.nimblesoftwares.ttrilha_api.application.user.exception.FriendshipPersistenceException;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipSolicitationRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipSolicitation;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FriendshipSolicitationJpaRepositoryAdapter implements FriendshipSolicitationRepositoryPort {

  private final FriendshipSolicitationMapper mapper;
  private final FriendshipSolicitationJpaRepository repository;

  public FriendshipSolicitationJpaRepositoryAdapter(
      FriendshipSolicitationMapper mapper,
      FriendshipSolicitationJpaRepository repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public FriendshipSolicitation save(FriendshipSolicitation solicitation) {
    try {
      FriendshipSolicitationEntity entity = mapper.toPersistence(solicitation);
      FriendshipSolicitationEntity saved = repository.save(entity);
      return mapper.toDomain(saved);
    } catch (JpaSystemException e) {
      throw new FriendshipPersistenceException("An error occurred while saving the solicitation. Please try again.");
    }
  }

  @Override
  public Optional<FriendshipSolicitation> findById(UUID id) {
    try {
      return repository.findById(id).map(mapper::toDomain);
    } catch (JpaSystemException e) {
      throw new FriendshipPersistenceException("An error occurred. Please try again.");
    }
  }

  @Override
  public boolean existsPendingBetween(UUID requesterId, UUID addresseeId) {
    return repository.existsByRequesterIdAndAddresseeIdAndStatus(requesterId, addresseeId, FriendshipStatus.PENDING);
  }

  @Override
  public List<FriendshipSolicitation> findPendingByAddresseeId(UUID addresseeId) {
    try {
      return repository.findByAddresseeIdAndStatus(addresseeId, FriendshipStatus.PENDING)
          .stream()
          .map(mapper::toDomain)
          .toList();
    } catch (JpaSystemException e) {
      throw new FriendshipPersistenceException("An error occurred. Please try again.");
    }
  }

  @Override
  public void cancelPendingBetween(UUID userA, UUID userB) {
    try {
      repository.cancelPendingBetween(userA, userB);
    } catch (JpaSystemException e) {
      throw new FriendshipPersistenceException("An error occurred while cancelling solicitations. Please try again.");
    }
  }
}
