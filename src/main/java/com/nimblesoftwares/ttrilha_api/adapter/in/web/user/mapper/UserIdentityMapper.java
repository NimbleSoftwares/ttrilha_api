package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserIdentityEntity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import org.springframework.stereotype.Component;

@Component
public class UserIdentityMapper {

  private final UserIdentityIdMapper userIdentityIdMapper;
  private final UserMapper userMapper;

  public UserIdentityMapper(UserIdentityIdMapper userIdentityIdMapper, UserMapper userMapper) {
    this.userIdentityIdMapper = userIdentityIdMapper;
    this.userMapper = userMapper;
  }

  public UserIdentityEntity toPersistence(UserIdentity userIdentity) {

    if(userIdentity == null) return null;

    return UserIdentityEntity.builder()
        .id(userIdentityIdMapper.toPersistence(userIdentity.getId()))
        .user(userMapper.toPersistence(userIdentity.getUser()))
        .build();
  }

  public UserIdentity toDomain(UserIdentityEntity userIdentityEntity) {
    if (userIdentityEntity == null) return null;

    return new UserIdentity(
        userIdentityIdMapper.toDomain(userIdentityEntity.getId()),
        userMapper.toDomain(userIdentityEntity.getUser()),
        userIdentityEntity.getCreatedAt(),
        userIdentityEntity.getUpdatedAt(),
        userIdentityEntity.getDeletedAt()
    );
  }
}
