package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_identities")
public class UserIdentityEntity {

  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;

  @ColumnDefault("now()")
  @Column(name = "updated_at", nullable = false, insertable = false)
  private OffsetDateTime updatedAt;

  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime createdAt;

  @MapsId(value = "userId")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "user_id", nullable = false)
  private UserEntity user;

  @EmbeddedId
  private UserIdentityEntityId id;

}
