package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities;

import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friendship_solicitations")
public class FriendshipSolicitationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "requester_id", nullable = false)
  private UUID requesterId;

  @Column(name = "addressee_id", nullable = false)
  private UUID addresseeId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  private FriendshipStatus status;

  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime createdAt;

  @ColumnDefault("now()")
  @Column(name = "updated_at", nullable = false, insertable = false)
  private OffsetDateTime updatedAt;

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = OffsetDateTime.now();
  }
}
