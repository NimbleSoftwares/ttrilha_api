package com.nimblesoftwares.ttrilha_api.application.expedition.command;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UpdateExpeditionCommand(
    UUID expeditionId,
    UUID currentUserId,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    // Optional trail update — null means no trail change
    Long osmId,
    String nameTrail,
    Map<String, String> tags,
    List<GeoPoint> geometry,
    // Members to mark as REMOVED
    List<UUID> removedMemberIds
) {}

