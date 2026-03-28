package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.impl;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities.TrailEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.interfaces.TrailJpaCustomRepository;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailDifficulty;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TrailJpaCustomRepositoryAdapter implements TrailJpaCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public UUID insertTrail(
      Long osmId,
      String name,
      String tags,
      TrailDifficulty difficulty,
      LineString geometry
  ) {

    String sql = """
        INSERT INTO trails (
            id,
            osm_id,
            name,
            tags,
            difficulty,
            geometry,
            geometry_simplified
        )
        VALUES (
            gen_random_uuid(),
            :osmId,
            :name,
            CAST(:tags AS jsonb),
            CAST(:difficulty AS varchar),
            :geometry,
            ST_Transform(
                ST_SimplifyPreserveTopology(
                    ST_Transform(CAST(:geometry AS geometry), 3857),
                    20
                ),
                4326
            )::geography
        )
        RETURNING id
    """;

    Object result = entityManager.createNativeQuery(sql)
        .setParameter("osmId", osmId)
        .setParameter("name", name)
        .setParameter("tags", tags)
        .setParameter("difficulty", difficulty.name())
        .setParameter("geometry", geometry)
        .getSingleResult();

    return (UUID) result;
  }

  @SuppressWarnings("unchecked")
  public List<TrailEntity> findByNameFuzzy(String name) {

    String sql = """
        SELECT *
        FROM trails
        WHERE similarity(
            unaccent(lower(name)),
            unaccent(lower(:name))
        ) > 0.3
        ORDER BY similarity(
            unaccent(lower(name)),
            unaccent(lower(:name))
        ) DESC
        LIMIT 10
        """;

    return entityManager
        .createNativeQuery(sql, TrailEntity.class)
        .setParameter("name", name)
        .getResultList();
  }
}
