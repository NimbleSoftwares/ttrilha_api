package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrailJpaRepository extends JpaRepository<TrailEntity, UUID> {
  Optional<TrailEntity> findByOsmId(Long osmId);
}
