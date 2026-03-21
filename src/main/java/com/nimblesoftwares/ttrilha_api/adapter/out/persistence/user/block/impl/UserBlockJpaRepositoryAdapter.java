package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.entities.UserBlockEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.interfaces.UserBlockJpaRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.mapper.UserBlockMapper;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserBlockRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserAlreadyBlockedException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserBlock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserBlockJpaRepositoryAdapter implements UserBlockRepositoryPort {

  private final UserBlockMapper mapper;
  private final UserBlockJpaRepository repository;

  public UserBlockJpaRepositoryAdapter(UserBlockMapper mapper, UserBlockJpaRepository repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public UserBlock save(UserBlock block) {
    try {
      UserBlockEntity entity = mapper.toPersistence(block);
      UserBlockEntity saved = repository.save(entity);
      return mapper.toDomain(saved);
    } catch (DataIntegrityViolationException e) {
      throw new UserAlreadyBlockedException("This user is already blocked.");
    }
  }

  @Override
  public boolean existsBlock(UUID blockerId, UUID blockedId) {
    return repository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
  }

  @Override
  public void delete(UUID blockerId, UUID blockedId) {
    repository.deleteByBlockerIdAndBlockedId(blockerId, blockedId);
  }
}

