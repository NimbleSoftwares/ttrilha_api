package com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OverpassElement(
    String type,
    Long id,
    Map<String, String> tags,
    List<GeoPoint> geometry,
    List<OverpassMember> members  // For relations
) {

  public OverpassElement(String type, Long id, Map<String, String> tags, List<GeoPoint> geometry) {
    this(type, id, tags, geometry, null);
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record OverpassMember(String type, Long ref, List<GeoPoint> geometry) {}
}
