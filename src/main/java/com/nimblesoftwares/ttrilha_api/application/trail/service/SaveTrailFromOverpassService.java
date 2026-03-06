package com.nimblesoftwares.ttrilha_api.application.trail.service;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.LineStringMapper;
import com.nimblesoftwares.ttrilha_api.application.trail.command.SaveTrailCommand;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;
import org.locationtech.jts.geom.LineString;

import java.util.Optional;
import java.util.UUID;

public class SaveTrailFromOverpassService implements SaveTrailUseCase {

  private final LineStringMapper lineStringMapper;
  private final TrailRepositoryPort trailRepositoryPort;

  public SaveTrailFromOverpassService(LineStringMapper lineStringMapper, TrailRepositoryPort trailRepositoryPort) {
    this.lineStringMapper = lineStringMapper;
    this.trailRepositoryPort = trailRepositoryPort;
  }

  @Override
  public UUID execute(SaveTrailCommand command) {

    Optional<Trail> existingTrail = trailRepositoryPort.findByOsmId(command.id());

    if(existingTrail.isPresent()) {
      return existingTrail.get().getId();
    }

    LineString original = lineStringMapper.toLineString(command.geometry());
    LineString simplified = lineStringMapper.simplify(original, 0.0001);

    Trail trail = new Trail();
    trail.setOsmId(command.id());
    trail.setName(command.name());
    trail.setTags(command.tags());
    trail.setGeometry(simplified);

    return trailRepositoryPort.save(trail);
  }
}
