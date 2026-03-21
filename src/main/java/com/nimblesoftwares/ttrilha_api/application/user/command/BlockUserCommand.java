package com.nimblesoftwares.ttrilha_api.application.user.command;

import java.util.UUID;

public record BlockUserCommand(UUID blockerId, UUID blockedId) {}

