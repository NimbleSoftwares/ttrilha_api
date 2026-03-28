package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassElement;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class OverpassMapper {

  private final LineStringMapper lineStringMapper;

  public OverpassMapper(LineStringMapper lineStringMapper) {
    this.lineStringMapper = lineStringMapper;
  }

  public Trail toDomain(OverpassElement element) {
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

    Trail trail = new Trail();
    trail.setOsmId(element.id());
    trail.setName(name);
    trail.setTags(element.tags());
    trail.setGeometry(lineStringMapper.toLineString(points));
    trail.setDifficulty(difficulty);

    return trail;
  }
}
