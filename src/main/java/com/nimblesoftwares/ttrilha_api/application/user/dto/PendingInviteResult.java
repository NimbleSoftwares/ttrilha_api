package com.nimblesoftwares.ttrilha_api.application.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PendingInviteResult(
    UUID solicitationId,
    UUID requesterId,
    String requesterDisplayName,
    String requesterFirstName,
    String requesterLastName,
    String requesterPicture,
    OffsetDateTime createdAt
) {}

