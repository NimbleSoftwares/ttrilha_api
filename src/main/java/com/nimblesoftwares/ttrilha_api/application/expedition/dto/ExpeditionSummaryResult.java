package com.nimblesoftwares.ttrilha_api.application.expedition.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpeditionSummaryResult(
    UUID id,
    String title,
    String trailName,
    Double distanceMeters,
    LocalDate startDate,
    LocalDate endDate,
    List<MemberInfo> members
) {}
