package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Arrays;
import java.util.List;

public class LineStringMapper {

  public LineString toLineString(List<GeoPoint> points) {

    GeometryFactory geometryFactory = new GeometryFactory(
        new PrecisionModel(),
        4326
    );

    Coordinate[] coords = points.stream()
        .map(point -> new Coordinate(point.lon(), point.lat()))
        .toArray(Coordinate[]::new);

    return geometryFactory.createLineString(coords);
  }

  public List<GeoPoint> toGeoPoints(LineString lineString) {
    return Arrays.stream(lineString.getCoordinates())
        .map(coord -> new GeoPoint(coord.y, coord.x))
        .toList();
  }
}
