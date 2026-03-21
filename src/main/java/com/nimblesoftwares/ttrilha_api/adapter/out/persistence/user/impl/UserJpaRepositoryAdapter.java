package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.impl;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserEntity;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper.UserMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.interfaces.UserJpaRepository;
import com.nimblesoftwares.ttrilha_api.application.user.exception.UserPersistenceException;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserJpaRepositoryAdapter implements UserRepositoryPort {

  private final UserMapper mapper;
  private final UserJpaRepository userJpaRepository;

  public UserJpaRepositoryAdapter(UserMapper mapper, UserJpaRepository userJpaRepository) {
    this.mapper = mapper;
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public User save(User user) {
    try {
      UserEntity saved = userJpaRepository.save(mapper.toPersistence(user));
      return mapper.toDomain(saved);
    } catch (DataIntegrityViolationException e) {
      throw new UserAlreadyExistsException("This user is already registered.");
    } catch (JpaSystemException e) {
      throw new UserPersistenceException("An error occurred while saving. Please try again.");
    }
  }

  @Override
  public Optional<User> findById(UUID id) {
    try {
      Optional<UserEntity> entity = userJpaRepository.findById(id);
      return entity.map(mapper::toDomain);
    } catch (JpaSystemException e) {
      throw new UserPersistenceException("An error occurred. Please try again.");
    }
  }

  @Override
  public Optional<User> findByUsername(String username) {
    try {
      return userJpaRepository.findByUsername(username).map(mapper::toDomain);
    } catch (JpaSystemException e) {
      throw new UserPersistenceException("An error occurred. Please try again.");
    }
  }
}
