package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.TrailDifficulty;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.LineString;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trails")
public class TrailEntity {

  @Column(name = "geometry", columnDefinition = "geography(LineString,4326)")
  private LineString geometry;

  @ColumnDefault("now()")
  @Column(name = "updated_at", insertable = false, updatable = false)
  private OffsetDateTime updatedAt;

  @ColumnDefault("now()")
  @Column(name = "created_at", insertable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Column(name = "elevation_gain")
  private Double elevationGain;

  @Column(name = "distance_meters", insertable = false, updatable = false)
  private Double distanceMeters;

  @Enumerated(EnumType.STRING)
  @Column(name = "difficulty")
  private TrailDifficulty difficulty;

  @Type(JsonType.class)
  @Column(name = "tags", columnDefinition = "jsonb")
  private Map<String, String> tags;

  @Size(max = 255)
  @Column(name = "name")
  private String name;

  @Column(name = "created_by_user_id")
  private UUID createdByUserId;

  @Column(name = "osm_id")
  private Long osmId;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;
}
