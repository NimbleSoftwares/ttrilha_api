package com.nimblesoftwares.ttrilha_api.application.trail.dto;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.SacScale;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailDifficulty;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailVisibility;

import java.util.List;
import java.util.Map;

public record TrailData(
    Long osmId,
    String name,
    Map<String, String> tags,
    TrailDifficulty difficulty,
    List<GeoPoint> geometry
) {

  public TrailDifficulty getDifficulty(String sacScale, String trailVisibility) {
    SacScale sacScaleEnum = SacScale.fromTag(sacScale);
    TrailVisibility trailVisibilityEnum  = TrailVisibility.fromTag(trailVisibility);

    return null;
  }
}
