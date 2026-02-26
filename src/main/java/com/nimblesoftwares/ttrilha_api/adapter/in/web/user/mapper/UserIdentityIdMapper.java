package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserIdentityEntityId;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentityId;
import org.springframework.stereotype.Component;

@Component
public class UserIdentityIdMapper {

  public UserIdentityEntityId toPersistence(UserIdentityId userIdentityId) {

    if (userIdentityId == null) {
      return null;
    }

    return new UserIdentityEntityId(
        userIdentityId.getUserId(),
        userIdentityId.getProvider(),
        userIdentityId.getProviderUserId()
    );
  }

  public UserIdentityId toDomain(UserIdentityEntityId userIdentityEntityId) {
    if (userIdentityEntityId == null) {
      return null;
    }

    return new UserIdentityId(
        userIdentityEntityId.getUserId(),
        userIdentityEntityId.getProvider(),
        userIdentityEntityId.getProviderUserId()
    );
  }
}
