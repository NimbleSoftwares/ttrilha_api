package com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto;

import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;
import jakarta.validation.constraints.NotNull;

public record RespondFriendshipInviteRequest(@NotNull FriendshipStatus status) {}
