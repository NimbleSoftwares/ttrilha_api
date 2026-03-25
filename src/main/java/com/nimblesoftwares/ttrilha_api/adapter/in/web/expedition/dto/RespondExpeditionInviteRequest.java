package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import jakarta.validation.constraints.NotNull;

public record RespondExpeditionInviteRequest(
    @NotNull MemberStatus status  // ACCEPTED or REJECTED
) {}

