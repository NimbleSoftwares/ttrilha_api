package com.nimblesoftwares.ttrilha_api.application.trail.port.out;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.Optional;
import java.util.UUID;

public interface TrailRepositoryPort {

  UUID save(Trail trail);
  Optional<Trail> findByOsmId(Long id);
}
