package com.nimblesoftwares.ttrilha_api.domain.expedition.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class Expedition {

  private UUID id;
  private String title;
  private Long osmId;
  private LocalDate startDate;
  private LocalDate endDate;
  private ExpeditionStatus status;
  private UUID createdByUserId;
  private List<UUID> memberIds;
  private OffsetDateTime createdAt;

  public Expedition() {}

  public UUID getId() { return id; }
  public void setId(UUID id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public Long getOsmId() { return osmId; }
  public void setOsmId(Long osmId) { this.osmId = osmId; }

  public LocalDate getStartDate() { return startDate; }
  public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

  public LocalDate getEndDate() { return endDate; }
  public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

  public ExpeditionStatus getStatus() { return status; }
  public void setStatus(ExpeditionStatus status) { this.status = status; }

  public UUID getCreatedByUserId() { return createdByUserId; }
  public void setCreatedByUserId(UUID createdByUserId) { this.createdByUserId = createdByUserId; }

  public List<UUID> getMemberIds() { return memberIds; }
  public void setMemberIds(List<UUID> memberIds) { this.memberIds = memberIds; }

  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
