package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionPendingInviteResult;

import java.util.UUID;

public record PendingExpeditionInviteResponse(
    UUID inviteId,
    UUID expeditionId,
    String expeditionTitle,
    UUID inviterId,
    String inviterDisplayName,
    String inviterAvatarUrl
) {
  public static PendingExpeditionInviteResponse fromResult(ExpeditionPendingInviteResult result) {
    return new PendingExpeditionInviteResponse(
        result.inviteId(),
        result.expeditionId(),
        result.expeditionTitle(),
        result.inviterId(),
        result.inviterDisplayName(),
        result.inviterAvatarUrl()
    );
  }
}

