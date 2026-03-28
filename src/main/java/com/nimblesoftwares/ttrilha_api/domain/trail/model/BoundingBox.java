package com.nimblesoftwares.ttrilha_api.domain.trail.model;

public class BoundingBox {

  private static final double EARTH_RADIUS_KM = 6371.0;

  private GeoPoint originalCoordinates;
  private double originalRadiusKm;
  private final double south;
  private final double west;
  private final double north;
  private final double east;

  public BoundingBox(double south, double west, double north, double east) {
    this.south = south;
    this.west = west;
    this.north = north;
    this.east = east;
  }

  public BoundingBox(GeoPoint originalCoordinates, double originalRadiusKm, double south, double west, double north, double east) {
    this.originalCoordinates = originalCoordinates;
    this.originalRadiusKm = originalRadiusKm;
    this.south = south;
    this.west = west;
    this.north = north;
    this.east = east;
  }

  public static BoundingBox fromPointAndRadius(GeoPoint originalCoordinates, double radiusKm) {

    double deltaLat = Math.toDegrees(radiusKm / EARTH_RADIUS_KM);
    double deltaLon = Math.toDegrees(
        radiusKm / EARTH_RADIUS_KM / Math.cos(Math.toRadians(originalCoordinates.lat()))
    );

    double south = originalCoordinates.lat() - deltaLat;
    double north = originalCoordinates.lat() + deltaLat;
    double west  = originalCoordinates.lon() - deltaLon;
    double east  = originalCoordinates.lon() + deltaLon;

    return new BoundingBox(originalCoordinates, radiusKm, south, west, north, east);
  }

  public String toTileKey() {
    double centerLat = (south + north) / 2;
    double centerLon = (west + east) / 2;

    int precision = resolvePrecision(originalRadiusKm);

    double factor = Math.pow(10, precision);

    long latTile = (long) Math.floor(centerLat * factor);
    long lonTile = (long) Math.floor(centerLon * factor);

    return precision + "_" + latTile + "_" + lonTile;
  }

  private int resolvePrecision(double radiusKm) {
    if (radiusKm <= 2) return 4;
    if (radiusKm <= 5) return 3;
    if (radiusKm <= 10) return 2;
    return 1;
  }

  public GeoPoint getOriginalCoordinates() {
    return originalCoordinates;
  }

  public void setOriginalCoordinates(GeoPoint originalCoordinates) {
    this.originalCoordinates = originalCoordinates;
  }

  public double getOriginalRadiusKm() {
    return originalRadiusKm;
  }

  public void setOriginalRadiusKm(double originalRadiusKm) {
    this.originalRadiusKm = originalRadiusKm;
  }

  public double getSouth() {
    return south;
  }

  public double getWest() {
    return west;
  }

  public double getNorth() {
    return north;
  }

  public double getEast() {
    return east;
  }
}
