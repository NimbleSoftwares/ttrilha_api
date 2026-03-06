package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;
import org.springframework.stereotype.Component;

@Component
public class TrailMapper {

  public TrailEntity toPersistence(Trail trail) {
    return TrailEntity.builder()
        .name(trail.getName())
        .osmId(trail.getOsmId())
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
    trail.setDifficulty(trailEntity.getDifficulty());
    trail.setElevationGain(trailEntity.getElevationGain());
    trail.setDistanceMeters(trailEntity.getDistanceMeters());
    trail.setGeometry(trailEntity.getGeometry());
    return trail;
  }
}
