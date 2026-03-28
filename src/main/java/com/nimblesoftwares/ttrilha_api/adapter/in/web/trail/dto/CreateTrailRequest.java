package com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto;

import com.nimblesoftwares.ttrilha_api.application.trail.command.SaveTrailCommand;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record CreateTrailRequest(
    Long osmId,
    String name,
    Map<String, String> tags,
    String difficulty,
    List<GeoPoint> geometry
) {

  public SaveTrailCommand toCommand() {
    return new SaveTrailCommand(name, osmId, tags, difficulty, geometry);
  }
}
