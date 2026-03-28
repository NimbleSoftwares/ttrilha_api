package com.nimblesoftwares.ttrilha_api.application.expedition.command;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateExpeditionCommand(
    String title,
    Long osmId,
    String nameTrail,
    Map<String, String> tags,
    String difficulty,
    List<GeoPoint> geometry,
    LocalDate startDate,
    LocalDate endDate,
    List<UUID> crewIds,
    UUID createdByUserId
) {}

