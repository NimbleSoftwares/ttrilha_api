package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.entities.UserBlockEntity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserBlock;
import org.springframework.stereotype.Component;

@Component
public class UserBlockMapper {

  public UserBlockEntity toPersistence(UserBlock block) {
    return UserBlockEntity.builder()
        .id(block.getId())
        .blockerId(block.getBlockerId())
        .blockedId(block.getBlockedId())
        .build();
  }

  public UserBlock toDomain(UserBlockEntity entity) {
    return new UserBlock(
        entity.getId(),
        entity.getBlockerId(),
        entity.getBlockedId(),
        entity.getCreatedAt()
    );
  }
}

