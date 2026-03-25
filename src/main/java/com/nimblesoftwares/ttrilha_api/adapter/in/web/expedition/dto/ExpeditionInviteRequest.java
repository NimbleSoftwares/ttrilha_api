package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ExpeditionInviteRequest(
    @NotNull UUID inviteeId
) {}

