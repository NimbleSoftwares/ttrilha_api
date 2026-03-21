package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto;

import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

public record UserSearchResponse(
    String id,
    String displayName,
    String username,
    String avatarUrl
) {
  public static UserSearchResponse fromDomain(User user) {
    return new UserSearchResponse(
        user.getId().toString(),
        user.getDisplayName(),
        user.getUsername(),
        user.getAvatarUrl()
    );
  }
}
