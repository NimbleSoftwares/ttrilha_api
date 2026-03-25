package com.nimblesoftwares.ttrilha_api.application.expedition.dto;

import java.util.UUID;

public record ExpeditionPendingInviteResult(
    UUID inviteId,
    UUID expeditionId,
    String expeditionTitle,
    UUID inviterId,
    String inviterDisplayName,
    String inviterAvatarUrl
) {}

