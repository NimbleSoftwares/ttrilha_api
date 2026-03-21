package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserEntity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public UserEntity toPersistence(User user) {

    if(user == null) return null;

    return UserEntity.builder()
        .id(user.getId())
        .email(user.getEmail())
        .displayName(user.getDisplayName())
        .lastName(user.getLastName())
        .firstName(user.getFirstName())
        .avatarUrl(user.getAvatarUrl())
        .username(user.getUsername())
        .build();
  }

  public User toDomain(UserEntity userEntity) {
    if (userEntity == null) return null;

    return new User(
        userEntity.getId(),
        userEntity.getEmail(),
        userEntity.getDisplayName(),
        userEntity.getFirstName(),
        userEntity.getLastName(),
        userEntity.getAvatarUrl(),
        userEntity.getUsername(),
        userEntity.getCreatedAt(),
        userEntity.getUpdatedAt(),
        userEntity.getDeletedAt()
    );
  }
}
