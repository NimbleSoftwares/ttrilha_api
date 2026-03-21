package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserIdBySubUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;

import java.util.UUID;

public class GetUserIdBySubService implements GetUserIdBySubUseCase {

  private final UserIdentityRepositoryPort userIdentityRepository;

  public GetUserIdBySubService(UserIdentityRepositoryPort userIdentityRepository) {
    this.userIdentityRepository = userIdentityRepository;
  }

  @Override
  public UUID execute(ProviderEnum provider, String providerUserId) {
    return userIdentityRepository
        .findByProviderAndProviderUserId(provider, providerUserId)
        .map(identity -> identity.getId().getUserId())
        .orElseThrow(() -> new UserNotFoundException("Authenticated user not found."));
  }
}
