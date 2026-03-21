package com.nimblesoftwares.ttrilha_api.domain.user.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class FriendshipSolicitation {

  private UUID id;
  private UUID requesterId;
  private UUID addresseeId;
  private FriendshipStatus status;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

  public FriendshipSolicitation() {}

  public FriendshipSolicitation(UUID requesterId, UUID addresseeId) {
    this.requesterId = requesterId;
    this.addresseeId = addresseeId;
    this.status = FriendshipStatus.PENDING;
  }

  public FriendshipSolicitation(UUID id, UUID requesterId, UUID addresseeId, FriendshipStatus status,
      OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    this.id = id;
    this.requesterId = requesterId;
    this.addresseeId = addresseeId;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public UUID getRequesterId() { return requesterId; }
  public void setRequesterId(UUID requesterId) { this.requesterId = requesterId; }

  public UUID getAddresseeId() { return addresseeId; }
  public void setAddresseeId(UUID addresseeId) { this.addresseeId = addresseeId; }

  public FriendshipStatus getStatus() { return status; }
  public void setStatus(FriendshipStatus status) { this.status = status; }

  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

  public OffsetDateTime getUpdatedAt() { return updatedAt; }
  public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
