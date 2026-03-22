package com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto;

import com.nimblesoftwares.ttrilha_api.application.user.dto.FriendResult;

import java.time.OffsetDateTime;

public record FriendResponse(
    String friendId,
    String username,
    String displayName,
    String firstName,
    String lastName,
    String avatarUrl,
    String city,
    String state,
    OffsetDateTime friendsSince
) {

  public static FriendResponse fromDomain(FriendResult result) {
    return new FriendResponse(
        result.friendId().toString(),
        result.username(),
        result.displayName(),
        result.firstName(),
        result.lastName(),
        result.avatarUrl(),
        result.city(),
        result.state(),
        result.friendsSince()
    );
  }
}
