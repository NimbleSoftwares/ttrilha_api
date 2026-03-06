package com.nimblesoftwares.ttrilha_api.application.trail.result;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record ExploreTrailResult(List<TrailSummary> trails) {
  public record TrailSummary(
      Long osmId,
      String name,
      Map<String, String> tags,
      List<GeoPoint> points
      ) {}
}
