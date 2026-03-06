package com.nimblesoftwares.ttrilha_api.application.trail.result;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.util.List;
import java.util.Map;

public record OverpassTrailData(
    Long osmId,
    Map<String, String> tags,
    List<GeoPoint> geometry
) {}
