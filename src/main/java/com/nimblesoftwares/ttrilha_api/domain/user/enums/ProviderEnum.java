package com.nimblesoftwares.ttrilha_api.domain.user.enums;

public enum ProviderEnum {
  GOOGLE,
  APPLE;

  public static ProviderEnum fromProviderSubPrefix(String prefix) {
    return switch (prefix.toLowerCase()) {
      case "google-oauth2" -> GOOGLE;
      case "apple"         -> APPLE;
      default              -> throw new IllegalArgumentException("Invalid provider: " + prefix);
    };
  }
}
