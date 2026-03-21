package com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto;

import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;

import java.time.OffsetDateTime;

public record FriendResponse(String friendId, OffsetDateTime createdAt) {

  public static FriendResponse fromDomain(Friendship friendship) {
    return new FriendResponse(
        friendship.getFriendId().toString(),
        friendship.getCreatedAt()
    );
  }
}
