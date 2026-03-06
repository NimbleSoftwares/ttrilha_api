package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.locationtech.jts.geom.LineString;

import java.time.OffsetDateTime;
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
  @Column(name = "updated_at")
  private OffsetDateTime updatedAt;

  @ColumnDefault("now()")
  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @Column(name = "elevation_gain")
  private Double elevationGain;

  @Column(name = "distance_meters")
  private Double distanceMeters;

  @Size(max = 50)
  @Column(name = "difficulty", length = 50)
  private String difficulty;

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
