package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

public interface GeocodingPort {

  record LatLon(double lat, double lon) {}

  LatLon geocode(String locationName);
}
