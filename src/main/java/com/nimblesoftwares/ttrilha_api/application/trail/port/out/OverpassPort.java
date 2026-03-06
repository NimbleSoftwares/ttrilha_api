package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

import com.nimblesoftwares.ttrilha_api.application.trail.dto.ExploreTrailResult;

public interface OverpassPort {

  ExploreTrailResult searchTrails(double lat, double lon, int radiusKm);
}
