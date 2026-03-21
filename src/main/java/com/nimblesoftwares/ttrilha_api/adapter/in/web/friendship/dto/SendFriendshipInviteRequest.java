package com.nimblesoftwares.ttrilha_api.adapter.in.web.friendship.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SendFriendshipInviteRequest(@NotNull UUID addresseeId) {}
