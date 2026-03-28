package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailDifficulty;
import org.locationtech.jts.geom.LineString;

import java.util.List;
import java.util.UUID;

public interface TrailJpaCustomRepository {
  UUID insertTrail(Long osmId, String name, String tags, TrailDifficulty difficulty, LineString geometry);
  List<TrailEntity> findByNameFuzzy(String name);
}
