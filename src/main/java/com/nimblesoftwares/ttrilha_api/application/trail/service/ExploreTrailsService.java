package com.nimblesoftwares.ttrilha_api.application.trail.service;

import com.nimblesoftwares.ttrilha_api.application.trail.command.ExploreTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.ExploreTrailResult;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.ExploreTrailsUsecase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;

public class ExploreTrailsService implements ExploreTrailsUsecase {

  private final OverpassPort overpassPort;
  private final GeocodingPort geocodingPort;

  public ExploreTrailsService(OverpassPort overpassPort, GeocodingPort geocodingPort) {
    this.overpassPort = overpassPort;
    this.geocodingPort = geocodingPort;
  }

  @Override
  public ExploreTrailResult execute(ExploreTrailCommand command) {

    double lat = command.lat() != null ? command.lat().doubleValue() : 0;
    double lon = command.lon() != null ? command.lon().doubleValue() : 0;

    if (command.lat() == null || command.lon() == null) {
      GeocodingPort.LatLon coords = geocodingPort.geocode
          (command.locationName());
      if (coords == null) {
        return null;
      }
      lat = coords.lat();
      lon = coords.lon();
    }

    return overpassPort.searchTrails(lat, lon, command.radiusKm());
  }
}
