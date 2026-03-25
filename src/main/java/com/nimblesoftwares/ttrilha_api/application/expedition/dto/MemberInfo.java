package com.nimblesoftwares.ttrilha_api.application.expedition.dto;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

import java.util.UUID;

public record MemberInfo(
    UUID inviteId,
    UUID id,
    String displayName,
    String avatarUrl,
    MemberStatus status
) {}

