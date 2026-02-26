package com.nimblesoftwares.ttrilha_api.domain.user.model;

import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;

import java.util.UUID;

public class UserIdentityId {

  private UUID userId;
  private ProviderEnum provider;
  private String providerUserId;

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public ProviderEnum getProvider() {
    return provider;
  }

  public void setProvider(ProviderEnum provider) {
    this.provider = provider;
  }

  public String getProviderUserId() {
    return providerUserId;
  }

  public void setProviderUserId(String providerUserId) {
    this.providerUserId = providerUserId;
  }
}
