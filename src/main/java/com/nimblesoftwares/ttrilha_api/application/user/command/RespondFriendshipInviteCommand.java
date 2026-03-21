package com.nimblesoftwares.ttrilha_api.application.user.command;

import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;

import java.util.UUID;

public record RespondFriendshipInviteCommand(UUID solicitationId, UUID responderId, FriendshipStatus status) {}
