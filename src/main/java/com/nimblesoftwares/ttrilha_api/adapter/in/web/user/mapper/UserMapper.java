package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserEntity;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.exception.InvalidJwtClaimsException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import org.springframework.security.oauth2.jwt.Jwt;
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
        userEntity.getCreatedAt(),
        userEntity.getUpdatedAt(),
        userEntity.getDeletedAt()
    );
  }


  public SaveUserCommand saveUserCommandfromJwt(Jwt jwt) {
    String sub = jwt.getSubject();
    String name = jwt.getClaimAsString("name");
    String givenName = jwt.getClaimAsString("given_name");
    String familyName = jwt.getClaimAsString("family_name");

    if (sub == null || name == null || givenName == null || familyName == null) {
      throw new InvalidJwtClaimsException("Invalid claims");
    }

    String email = jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "";
    String picture = jwt.getClaimAsString("picture") != null ? jwt.getClaimAsString("picture") : "";

    ProviderIdentity providerIdentity = ProviderIdentity.fromSub(sub);

    return new SaveUserCommand(email, name, givenName, familyName, picture,
        providerIdentity.provider(), providerIdentity.providerUserId());
  }
}
