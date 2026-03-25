package com.nimblesoftwares.ttrilha_api.application.expedition.command;

import java.util.UUID;

public record InviteToExpeditionCommand(
    UUID expeditionId,
    UUID inviterId,
    UUID inviteeId
) {}

