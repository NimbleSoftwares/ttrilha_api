package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserIdentityEntity;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserIdentityEntityId;
import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIdentityJpaRepository extends JpaRepository<UserIdentityEntity, UserIdentityEntityId> {

  Optional<UserIdentityEntity> findByIdProviderAndIdProviderUserId(
      ProviderEnum provider, String providerUserId);
}