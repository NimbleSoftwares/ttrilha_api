package com.nimblesoftwares.ttrilha_api.application.trail.service;

import com.nimblesoftwares.ttrilha_api.application.trail.command.ExploreTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.ExploreTrailResult;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.ExploreTrailsUsecase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.BoundingBox;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.List;

public class ExploreTrailsService implements ExploreTrailsUsecase {

  private final OverpassPort overpassPort;
  private final GeocodingPort geocodingPort;
  private final TrailRepositoryPort trailRepositoryPort;

  public ExploreTrailsService(OverpassPort overpassPort, GeocodingPort geocodingPort, TrailRepositoryPort trailRepositoryPort) {
    this.overpassPort = overpassPort;
    this.geocodingPort = geocodingPort;
    this.trailRepositoryPort = trailRepositoryPort;
  }

  @Override
  public ExploreTrailResult execute(ExploreTrailCommand command) {

    List<Trail> trails = trailRepositoryPort.findByNameFuzzy(command.locationName());

    if (!trails.isEmpty()) {
      return ExploreTrailResult.from(trails);
    }

    double lat = command.lat() != null ? command.lat().doubleValue() : 0;
    double lon = command.lon() != null ? command.lon().doubleValue() : 0;

    if (lat == 0 || lon == 0) {
      GeocodingPort.LatLon coords = geocodingPort.geocode
          (command.locationName());
      if (coords == null) {
        return null;
      }
      lat = coords.lat();
      lon = coords.lon();
    }

    BoundingBox bbox = BoundingBox.fromPointAndRadius(new GeoPoint(lat, lon), command.radiusKm());

    return ExploreTrailResult.from(overpassPort.searchTrails(bbox));
  }
}
