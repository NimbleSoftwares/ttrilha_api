package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.TrailData;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class TrailMapper {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public TrailEntity toPersistence(Trail trail) {
    return TrailEntity.builder()
        .name(trail.getName())
        .osmId(trail.getOsmId())
        .tags(trail.getTags())
        .difficulty(trail.getDifficulty())
        .elevationGain(trail.getElevationGain())
        .distanceMeters(trail.getDistanceMeters())
        .geometry(trail.getGeometry())
        .build();
  }

  public Trail toDomain(TrailEntity trailEntity) {
    Trail trail = new Trail();
    trail.setId(trailEntity.getId());
    trail.setName(trailEntity.getName());
    trail.setOsmId(trailEntity.getOsmId());
    trail.setTags(trailEntity.getTags());
    trail.setDifficulty(trailEntity.getDifficulty());
    trail.setElevationGain(trailEntity.getElevationGain());
    trail.setDistanceMeters(trailEntity.getDistanceMeters());
    trail.setGeometry(trailEntity.getGeometry());
    return trail;
  }

  public String mapTagsToJson(Map<String, String> tags) {
    try {
      return objectMapper.writeValueAsString(tags);
    } catch (Exception e) {
      throw new RuntimeException("Error converting to JSON", e);
    }
  }

  public TrailData entityToTrailData(TrailEntity entity) {
      List<GeoPoint> points =
          Arrays.stream(
              entity.getGeometry()
                  .getCoordinates()).map(
                      coord -> new GeoPoint(coord.y, coord.x)
          ).toList();

      return new TrailData(
          entity.getOsmId(),
          entity.getName(),
          entity.getTags(),
          entity.getDifficulty(),
          points);
  }
}
