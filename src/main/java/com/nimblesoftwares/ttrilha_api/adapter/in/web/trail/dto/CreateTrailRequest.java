package com.nimblesoftwares.ttrilha_api.adapter.in.web.trail.dto;

import com.nimblesoftwares.ttrilha_api.application.trail.command.SaveTrailCommand;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record CreateTrailRequest(
    String type,
    Long id,
    Map<String, String> tags,
    List<GeoPoint> geometry
) {

  public SaveTrailCommand toCommand() {
    return new SaveTrailCommand(type, id, tags, geometry);
  }
}
