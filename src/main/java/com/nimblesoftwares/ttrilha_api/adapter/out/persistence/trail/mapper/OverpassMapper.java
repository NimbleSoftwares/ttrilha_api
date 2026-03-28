package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassElement;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.TrailData;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.SacScale;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailDifficulty;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailVisibility;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OverpassMapper {

  public TrailData toTrailData(OverpassElement element) {
    String name = element.tags() != null ? element.tags().getOrDefault("name", "Unnamed") : "Unnamed";

    SacScale sacScale = element.tags() != null ? SacScale.fromTag(element.tags().getOrDefault("sac_scale", "Unknown")) : SacScale.UNKNOWN;
    TrailVisibility trailVisibility = element.tags() != null ? TrailVisibility.fromTag(element.tags().getOrDefault("trail_visibility", "Unknown")) : TrailVisibility.UNKNOWN;

    boolean hasDifficultyKnown = sacScale != SacScale.UNKNOWN || trailVisibility != TrailVisibility.UNKNOWN;
    TrailDifficulty difficulty = hasDifficultyKnown ?
        TrailDifficulty.calculateDifficulty(sacScale, trailVisibility) : TrailDifficulty.UNKNOWN;

    List<GeoPoint> points = (
        element.geometry() != null && !element.geometry().isEmpty())
        ? element.geometry()
        : (element.members() != null)
        ? element.members().stream()
        .filter(m -> m.geometry() != null)
        .flatMap(m -> m.geometry().stream())
        .filter(Objects::nonNull)
        .toList()
        : List.of();

    if (points.size() < 2) {
      return null;
    }

    return new TrailData(element.id(), name, element.tags(), difficulty, points);
  }
}
