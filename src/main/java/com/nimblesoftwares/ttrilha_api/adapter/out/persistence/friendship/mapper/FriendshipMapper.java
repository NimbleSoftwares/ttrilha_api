package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities.FriendshipEntity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipMapper {

  public FriendshipEntity toPersistence(Friendship friendship) {
    return FriendshipEntity.builder()
        .id(friendship.getId())
        .userId(friendship.getUserId())
        .friendId(friendship.getFriendId())
        .solicitationId(friendship.getSolicitationId())
        .build();
  }

  public Friendship toDomain(FriendshipEntity entity) {
    return new Friendship(
        entity.getId(),
        entity.getUserId(),
        entity.getFriendId(),
        entity.getSolicitationId(),
        entity.getCreatedAt()
    );
  }
}
