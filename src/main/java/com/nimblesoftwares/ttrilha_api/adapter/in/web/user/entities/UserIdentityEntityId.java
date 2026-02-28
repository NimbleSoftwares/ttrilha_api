package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities;

import com.nimblesoftwares.ttrilha_api.domain.user.enums.ProviderEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class UserIdentityEntityId implements Serializable {

  @Serial
  private static final long serialVersionUID = -6496011023089830973L;

  @NotNull
  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(columnDefinition = "oauth_provider", nullable = false, length = 50)
  private ProviderEnum provider;

  @Size(max = 255)
  @NotNull
  @Column(name = "provider_user_id", nullable = false)
  private String providerUserId;

}