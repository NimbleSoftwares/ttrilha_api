package com.nimblesoftwares.ttrilha_api.domain.trail.model;

import org.locationtech.jts.geom.LineString;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public class Trail {

  private UUID id;
  private Long osmId;
  private String name;
  private Map<String, String> tags;
  private TrailDifficulty difficulty;
  private UUID createdByUserId;
  private Double distanceMeters;
  private Double elevationGain;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private LineString geometry;

  public Trail(){}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Long getOsmId() {
    return osmId;
  }

  public void setOsmId(Long osmId) {
    this.osmId = osmId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TrailDifficulty getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(TrailDifficulty difficulty) {
    this.difficulty = difficulty;
  }

  public UUID getCreatedByUserId() {
    return createdByUserId;
  }

  public void setCreatedByUserId(UUID createdByUserId) {
    this.createdByUserId = createdByUserId;
  }

  public Double getDistanceMeters() {
    return distanceMeters;
  }

  public void setDistanceMeters(Double distanceMeters) {
    this.distanceMeters = distanceMeters;
  }

  public Double getElevationGain() {
    return elevationGain;
  }

  public void setElevationGain(Double elevationGain) {
    this.elevationGain = elevationGain;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LineString getGeometry() {
    return geometry;
  }

  public void setGeometry(LineString geometry) {
    this.geometry = geometry;
  }

  public Map<String, String> getTags() {
    return tags;
  }

  public void setTags(Map<String, String> tags) {
    this.tags = tags;
  }
}
