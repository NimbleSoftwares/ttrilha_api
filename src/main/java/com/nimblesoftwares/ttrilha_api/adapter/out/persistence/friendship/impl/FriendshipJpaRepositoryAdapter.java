package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities.FriendshipEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.interfaces.FriendshipJpaRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.mapper.FriendshipMapper;
import com.nimblesoftwares.ttrilha_api.application.user.exception.FriendshipPersistenceException;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.FriendshipAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class FriendshipJpaRepositoryAdapter implements FriendshipRepositoryPort {

  private final FriendshipMapper mapper;
  private final FriendshipJpaRepository repository;

  public FriendshipJpaRepositoryAdapter(FriendshipMapper mapper, FriendshipJpaRepository repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public Friendship save(Friendship friendship) {
    try {
      FriendshipEntity entity = mapper.toPersistence(friendship);
      FriendshipEntity saved = repository.save(entity);
      return mapper.toDomain(saved);
    } catch (DataIntegrityViolationException e) {
      throw new FriendshipAlreadyExistsException("These users are already friends.", e);
    } catch (JpaSystemException e) {
      throw new FriendshipPersistenceException("An error occurred while saving the friendship. Please try again.");
    }
  }

  @Override
  public List<Friendship> findByUserId(UUID userId) {
    return repository.findByUserId(userId).stream()
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  public void deleteByBothSides(UUID userA, UUID userB) {
    try {
      repository.deleteByBothSides(userA, userB);
    } catch (JpaSystemException e) {
      throw new FriendshipPersistenceException("An error occurred while removing the friendship. Please try again.");
    }
  }
}
