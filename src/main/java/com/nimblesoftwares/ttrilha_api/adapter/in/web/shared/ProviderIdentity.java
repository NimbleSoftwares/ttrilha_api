package com.nimblesoftwares.ttrilha_api.adapter.in.web.shared;

import com.nimblesoftwares.ttrilha_api.domain.user.exception.InvalidProviderException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;

public record ProviderIdentity(ProviderEnum provider, String providerUserId) {

  public static ProviderIdentity fromSub(String sub) {
    if (sub == null || sub.isBlank()) {
      throw new InvalidProviderException("Invalid sub format: " + sub);
    }

    int idx = sub.indexOf('|');
    if (idx == -1 || idx == sub.length() - 1) {
      throw new InvalidProviderException("Invalid sub format: " + sub);
    }

    String prefix = sub.substring(0, idx);
    String providerUserId = sub.substring(idx + 1);

    return new ProviderIdentity(
        ProviderEnum.fromProviderSubPrefix(prefix),
        providerUserId
    );
  }
}
