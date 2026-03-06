package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.interfaces.TrailJpaRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.TrailMapper;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.trail.exception.TrailAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TrailJpaRepositoryAdapter implements TrailRepositoryPort {

  private final TrailMapper mapper;
  private final TrailJpaRepository trailJpaRepository;

  public TrailJpaRepositoryAdapter(TrailMapper mapper, TrailJpaRepository trailJpaRepository) {
    this.mapper = mapper;
    this.trailJpaRepository = trailJpaRepository;
  }

  @Override
  public UUID save(Trail trail) {
    TrailEntity trailEntity = mapper.toPersistence(trail);

    try {
      TrailEntity saved = trailJpaRepository.save(trailEntity);
      return saved.getId();
    } catch (DataIntegrityViolationException e) {
      throw new TrailAlreadyExistsException("Trail with osm_id " + trail.getOsmId() + " already exists.", e);
    }
  }

  @Override
  public Optional<Trail> findByOsmId(Long osmId) {
    return trailJpaRepository.findByOsmId(osmId).map(mapper::toDomain);
  }
}
