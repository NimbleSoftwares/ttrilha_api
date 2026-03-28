package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.interfaces.TrailJpaCustomRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.interfaces.TrailJpaRepository;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.TrailMapper;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.TrailData;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.trail.exception.TrailAlreadyExistsException;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TrailJpaRepositoryAdapter implements TrailRepositoryPort {

  private final TrailMapper mapper;
  private final TrailJpaRepository trailJpaRepository;
  private final TrailJpaCustomRepository customRepository;


  public TrailJpaRepositoryAdapter(TrailMapper mapper, TrailJpaRepository trailJpaRepository, TrailJpaCustomRepository customRepository) {
    this.mapper = mapper;
    this.trailJpaRepository = trailJpaRepository;
    this.customRepository = customRepository;
  }

  @Override
  public UUID save(Trail trail) {
    try {
      return customRepository.insertTrail(
          trail.getOsmId(),
          trail.getName(),
          mapper.mapTagsToJson(trail.getTags()),
          trail.getDifficulty(),
          trail.getGeometry()
      );
    } catch (DataIntegrityViolationException e) {
      throw new TrailAlreadyExistsException("Trail with osm_id " + trail.getOsmId() + " already exists.", e);
    }
  }

  @Override
  public Optional<Trail> findByOsmId(Long osmId) {
    return trailJpaRepository.findByOsmId(osmId).map(mapper::toDomain);
  }

  @Override
  public List<TrailData> findByNameFuzzy(String name) {
    List<TrailEntity> trailEntityList = customRepository.findByNameFuzzy(name);
    return trailEntityList.stream().map(mapper::entityToTrailData).toList();
  }
}
