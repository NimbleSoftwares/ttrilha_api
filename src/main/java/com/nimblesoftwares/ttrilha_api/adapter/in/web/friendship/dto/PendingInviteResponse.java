package com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto;

import com.nimblesoftwares.ttrilha_api.application.user.dto.PendingInviteResult;

import java.time.OffsetDateTime;

public record PendingInviteResponse(
    String solicitationId,
    String requesterId,
    String requesterDisplayName,
    String requesterFirstName,
    String requesterLastName,
    String requesterPicture,
    OffsetDateTime createdAt
) {

  public static PendingInviteResponse fromDomain(PendingInviteResult result) {
    return new PendingInviteResponse(
        result.solicitationId().toString(),
        result.requesterId().toString(),
        result.requesterDisplayName(),
        result.requesterFirstName(),
        result.requesterLastName(),
        result.requesterPicture(),
        result.createdAt()
    );
  }
}
