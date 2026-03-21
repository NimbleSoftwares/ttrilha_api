package com.nimblesoftwares.ttrilha_api.domain.user.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Friendship {

  private UUID id;
  private UUID userId;
  private UUID friendId;
  private UUID solicitationId;
  private OffsetDateTime createdAt;

  public Friendship() {}

  public Friendship(UUID userId, UUID friendId, UUID solicitationId) {
    this.userId = userId;
    this.friendId = friendId;
    this.solicitationId = solicitationId;
  }

  public Friendship(UUID id, UUID userId, UUID friendId, UUID solicitationId, OffsetDateTime createdAt) {
    this.id = id;
    this.userId = userId;
    this.friendId = friendId;
    this.solicitationId = solicitationId;
    this.createdAt = createdAt;
  }

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public UUID getUserId() { return userId; }
  public void setUserId(UUID userId) { this.userId = userId; }

  public UUID getFriendId() { return friendId; }
  public void setFriendId(UUID friendId) { this.friendId = friendId; }

  public UUID getSolicitationId() { return solicitationId; }
  public void setSolicitationId(UUID solicitationId) { this.solicitationId = solicitationId; }

  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
