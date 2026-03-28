package com.nimblesoftwares.ttrilha_api.application.trail.command;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record SaveTrailCommand(
    String name,
    Long osmId,
    Map<String, String> tags,
    String difficulty,
    List<GeoPoint> geometry
) {
}
