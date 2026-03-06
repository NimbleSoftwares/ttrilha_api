package com.nimblesoftwares.ttrilha_api.application.trail.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassResponse;
import com.nimblesoftwares.ttrilha_api.application.trail.command.ExploreTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.ExploreTrailsUsecase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;

public class ExploreTrailsService implements ExploreTrailsUsecase {

  private final ObjectMapper objectMapper;
  private final OverpassPort overpassPort;
  private final GeocodingPort geocodingPort;

  public ExploreTrailsService(ObjectMapper objectMapper, OverpassPort overpassPort, GeocodingPort geocodingPort) {
    this.objectMapper = objectMapper;
    this.overpassPort = overpassPort;
    this.geocodingPort = geocodingPort;
  }

  @Override
  public OverpassResponse execute(ExploreTrailCommand command) {

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

    String trails = overpassPort.searchTrails(lat, lon, command.radiusKm());

    try {
      OverpassResponse response =
          objectMapper.readValue(trails, OverpassResponse.class);
      return response;
    } catch (Exception e) {
      return null;
    }



//    List<TrailSummary> summaries = trails.stream()
//        .map(trail -> {
//          String name = trail.tags() != null ? trail.tags().getOrDefault("name", "Unnamed") : "Unnamed";
//
//          return new TrailSummary(
//              trail.osmId(),
//              name,
//              trail.tags(),
//              trail.geometry()
//          );
//        })
//        .toList();
//
//    return new ExploreTrailResult(summaries);
  }
}
