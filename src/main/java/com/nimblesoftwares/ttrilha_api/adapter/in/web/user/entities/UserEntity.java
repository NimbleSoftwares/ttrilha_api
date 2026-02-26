package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "deleted_at")
  private OffsetDateTime deletedAt;

  @ColumnDefault("now()")
  @Column(name = "updated_at", nullable = false, insertable = false)
  private OffsetDateTime updatedAt;

  @ColumnDefault("now()")
  @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
  private OffsetDateTime createdAt;

  @Size(max = 500)
  @Column(name = "avatar_url", length = 500)
  private String avatarUrl;

  @Size(max = 70)
  @NotNull
  @Column(name = "last_name", nullable = false, length = 70)
  private String lastName;

  @Size(max = 30)
  @NotNull
  @Column(name = "first_name", nullable = false, length = 30)
  private String firstName;

  @Size(max = 100)
  @NotNull
  @Column(name = "display_name", nullable = false, length = 100)
  private String displayName;

  @Size(max = 254)
  @NotNull
  @Column(name = "email", nullable = false, length = 254)
  private String email;

}
