package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

import com.nimblesoftwares.ttrilha_api.application.trail.dto.TrailData;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrailRepositoryPort {

  UUID save(Trail trail);
  Optional<Trail> findByOsmId(Long osmId);
  List<TrailData> findByNameFuzzy(String name);
}
