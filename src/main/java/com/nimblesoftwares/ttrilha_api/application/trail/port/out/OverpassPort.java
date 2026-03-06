package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

public interface OverpassPort {

  String searchTrails(double lat, double lon, int radiusKm);
}
