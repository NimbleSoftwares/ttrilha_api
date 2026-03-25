package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.entities;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expeditions")
public class ExpeditionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "osm_id", nullable = false)
  private Long osmId;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private ExpeditionStatus status;

  @Column(name = "created_by_user_id", nullable = false)
  private UUID createdByUserId;

  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime createdAt;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "expedition_members",
      joinColumns = @JoinColumn(name = "expedition_id")
  )
  private List<ExpeditionMemberEntity> members;

  // Explicit accessors to assist IDE when Lombok annotation processing is unavailable
  public UUID getId() { return id; }
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
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public List<ExpeditionMemberEntity> getMembers() { return members; }
  public void setMembers(List<ExpeditionMemberEntity> members) { this.members = members; }
}

