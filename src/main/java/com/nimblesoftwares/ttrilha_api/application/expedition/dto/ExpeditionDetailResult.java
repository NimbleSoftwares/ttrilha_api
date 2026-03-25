package com.nimblesoftwares.ttrilha_api.application.expedition.dto;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ExpeditionDetailResult(
    UUID id,
    String title,
    Long osmId,
    String trailName,
    LocalDate startDate,
    LocalDate endDate,
    ExpeditionStatus status,
    UUID createdByUserId,
    List<MemberInfo> members,
    List<com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint> geometry,
    OffsetDateTime createdAt
) {}
