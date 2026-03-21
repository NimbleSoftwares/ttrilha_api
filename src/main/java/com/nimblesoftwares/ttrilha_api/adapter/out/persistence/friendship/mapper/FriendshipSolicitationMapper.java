package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities.FriendshipSolicitationEntity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipSolicitation;
import org.springframework.stereotype.Component;

@Component
public class FriendshipSolicitationMapper {

  public FriendshipSolicitationEntity toPersistence(FriendshipSolicitation solicitation) {
    return FriendshipSolicitationEntity.builder()
        .id(solicitation.getId())
        .requesterId(solicitation.getRequesterId())
        .addresseeId(solicitation.getAddresseeId())
        .status(solicitation.getStatus())
        .build();
  }

  public FriendshipSolicitation toDomain(FriendshipSolicitationEntity entity) {
    return new FriendshipSolicitation(
        entity.getId(),
        entity.getRequesterId(),
        entity.getAddresseeId(),
        entity.getStatus(),
        entity.getCreatedAt(),
        entity.getUpdatedAt()
    );
  }
}
