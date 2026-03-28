package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import com.nimblesoftwares.ttrilha_api.application.expedition.command.CreateExpeditionCommand;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateExpeditionRequest(
    @NotBlank String title,
    @NotNull Long osmId,
    @NotBlank String nameTrail,
    Map<String, String> tags,
    String difficulty,
    @NotEmpty List<GeoPoint> geometry,
    @NotNull LocalDate startDate,
    @NotNull LocalDate endDate,
    List<UUID> crewIds
) {
  public CreateExpeditionCommand toCommand(UUID createdByUserId) {
    return new CreateExpeditionCommand(
        title,
        osmId,
        nameTrail,
        tags,
        difficulty,
        geometry,
        startDate,
        endDate,
        crewIds != null ? crewIds : List.of(),
        createdByUserId
    );
  }
}

