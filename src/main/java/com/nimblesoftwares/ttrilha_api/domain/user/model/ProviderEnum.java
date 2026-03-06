package com.nimblesoftwares.ttrilha_api.domain.user.model;

import com.nimblesoftwares.ttrilha_api.domain.user.exception.InvalidProviderException;

public enum ProviderEnum {
  GOOGLE,
  APPLE;

  public static ProviderEnum fromProviderSubPrefix(String prefix) {
    return switch (prefix.toLowerCase()) {
      case "google-oauth2" -> GOOGLE;
      case "apple"         -> APPLE;
      default              -> throw new InvalidProviderException("Invalid provider: " + prefix);
    };
  }
}
