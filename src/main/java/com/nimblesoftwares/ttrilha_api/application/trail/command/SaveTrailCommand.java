package com.nimblesoftwares.ttrilha_api.application.trail.command;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record SaveTrailCommand(
    String type,
    Long id,
    Map<String, String> tags,
    List<GeoPoint> geometry
) {
}
