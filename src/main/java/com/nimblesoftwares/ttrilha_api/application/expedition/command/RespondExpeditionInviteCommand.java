package com.nimblesoftwares.ttrilha_api.application.expedition.command;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

import java.util.UUID;

public record RespondExpeditionInviteCommand(
    UUID inviteId,
    UUID responderId,
    MemberStatus response   // ACCEPTED or REJECTED
) {}

