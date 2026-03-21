package com.nimblesoftwares.ttrilha_api.application.user.command;

import java.util.UUID;

public record UnblockUserCommand(UUID blockerId, UUID blockedId) {}

