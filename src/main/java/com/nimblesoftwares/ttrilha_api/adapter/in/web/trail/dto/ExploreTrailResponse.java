package com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto;

import com.nimblesoftwares.ttrilha_api.application.trail.result.ExploreTrailResult;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record ExploreTrailResponse(List<TrailSummary> trails) {

  public record TrailSummary(Long osmId, String name, Map<String, String> tags, List<GeoPoint> points) {}

  public static ExploreTrailResponse fromResult(ExploreTrailResult result) {
    List<TrailSummary> summaries = result.trails().stream()
        .map(ts -> new TrailSummary(ts.osmId(), ts.name(), ts.tags(), ts.points()))
        .toList();
    return new ExploreTrailResponse(summaries);
  }
}
