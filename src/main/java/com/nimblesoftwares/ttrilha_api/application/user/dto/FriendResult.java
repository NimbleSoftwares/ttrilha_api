package com.nimblesoftwares.ttrilha_api.application.user.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record FriendResult(
    UUID friendId,
    String username,
    String displayName,
    String firstName,
    String lastName,
    String avatarUrl,
    String city,
    String state,
    OffsetDateTime friendsSince
) {}

