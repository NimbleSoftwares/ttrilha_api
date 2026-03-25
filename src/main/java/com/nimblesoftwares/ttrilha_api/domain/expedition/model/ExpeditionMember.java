package com.nimblesoftwares.ttrilha_api.domain.expedition.model;

import java.util.UUID;

public class ExpeditionMember {

  private UUID inviteId;
  private UUID userId;
  private MemberStatus status;

  public ExpeditionMember() {}

  public ExpeditionMember(UUID inviteId, UUID userId, MemberStatus status) {
    this.inviteId = inviteId;
    this.userId = userId;
    this.status = status;
  }

  public UUID getInviteId() { return inviteId; }
  public void setInviteId(UUID inviteId) { this.inviteId = inviteId; }

  public UUID getUserId() { return userId; }
  public void setUserId(UUID userId) { this.userId = userId; }

  public MemberStatus getStatus() { return status; }
  public void setStatus(MemberStatus status) { this.status = status; }
}

