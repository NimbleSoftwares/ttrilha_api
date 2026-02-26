package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.impl;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserIdentityEntity;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper.UserIdentityMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.interfaces.UserIdentityJpaRepository;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserIdentityJpaRepositoryAdapter implements UserIdentityRepositoryPort {

  private final UserIdentityMapper mapper;
  private final UserIdentityJpaRepository userIdentityJpaRepository;

  public UserIdentityJpaRepositoryAdapter(UserIdentityMapper mapper, UserIdentityJpaRepository userIdentityJpaRepository) {
    this.mapper = mapper;
    this.userIdentityJpaRepository = userIdentityJpaRepository;
  }

  public UserIdentity save(UserIdentity userIdentity) {
    UserIdentityEntity entity = mapper.toPersistence(userIdentity);
    UserIdentityEntity saved = userIdentityJpaRepository.save(entity);

    return mapper.toDomain(saved);
  }

  @Override
  public Optional<UserIdentity> findByProviderAndProviderUserId(ProviderEnum provider, String providerUserId) {
    return userIdentityJpaRepository
        .findByIdProviderAndIdProviderUserId(provider, providerUserId)
        .map(mapper::toDomain);
  }
}