package com.nimblesoftwares.ttrilha_api.application.user.port.out;

import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;

import java.util.Optional;

public interface UserIdentityRepositoryPort {

  UserIdentity save(UserIdentity userIdentity);
  Optional<UserIdentity> findByProviderAndProviderUserId(ProviderEnum provider, String providerUserId);
}