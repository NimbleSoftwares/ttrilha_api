package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.entities;

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
@Table(name = "user_blocks")
public class UserBlockEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "blocker_id", nullable = false)
  private UUID blockerId;

  @Column(name = "blocked_id", nullable = false)
  private UUID blockedId;

  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime createdAt;
}

