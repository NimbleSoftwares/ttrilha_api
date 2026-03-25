package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.entities;

import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpeditionMemberEntity {

  @Column(name = "invite_id", nullable = false)
  private UUID inviteId;

  @Column(name = "user_id", nullable = false)
  private UUID userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private MemberStatus status;
}

