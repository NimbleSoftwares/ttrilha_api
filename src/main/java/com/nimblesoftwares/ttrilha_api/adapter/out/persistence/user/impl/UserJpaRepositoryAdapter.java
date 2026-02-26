package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.impl;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserEntity;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper.UserMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.interfaces.UserJpaRepository;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserJpaRepositoryAdapter implements UserRepositoryPort {

  private final UserMapper mapper;
  private final UserJpaRepository userJpaRepository;

  public UserJpaRepositoryAdapter(UserMapper mapper, UserJpaRepository userJpaRepository) {
    this.mapper = mapper;
    this.userJpaRepository = userJpaRepository;
  }

  public User save(User user) {
    UserEntity saved = userJpaRepository.save(mapper.toPersistence(user));
    return mapper.toDomain(saved);
  }
}
