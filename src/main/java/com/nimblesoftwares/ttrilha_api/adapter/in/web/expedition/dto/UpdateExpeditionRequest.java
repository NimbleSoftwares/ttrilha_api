package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.UpdateExpeditionCommand;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record UpdateExpeditionRequest(
    String title,
    LocalDate startDate,
    LocalDate endDate,
    // Optional trail change
    Long osmId,
    String nameTrail,
    Map<String, String> tags,
    List<GeoPoint> geometry,
    // Optional list of member userIds to remove
    List<UUID> removedMemberIds
) {
  public UpdateExpeditionCommand toCommand(UUID expeditionId, UUID currentUserId) {
    return new UpdateExpeditionCommand(
        expeditionId,
        currentUserId,
        title,
        startDate,
        endDate,
        osmId,
        nameTrail,
        tags,
        geometry,
        removedMemberIds
    );
  }
}

