package com.nimblesoftwares.ttrilha_api.user;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.dto.SaveUserRequest;
import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentityId;

public class TestUserBuilders {

  private static final String GOOGLE_ID = "12332103213912031321";
  private static final ProviderEnum PROVIDER = ProviderEnum.GOOGLE;

  static SaveUserRequest createSaveUserRequest(Boolean isInvalid) {

    if (isInvalid) {
      return new SaveUserRequest(
          "",
          "testgmail.com",
          "Luan",
          "Marcene",
          ""
      );
    }
    return new SaveUserRequest(
        "Luan Marcene",
        "test@gmail.com",
        "Luan",
        "Marcene",
        ""
    );
  }

  static SaveUserCommand createCommand() {
    return new SaveUserCommand(
        "email_example@gmail.com",
        "DisplayName",
        "FirstName",
        "LastName",
        null,
        PROVIDER,
        GOOGLE_ID
    );
  }

  static UserIdentity createUserIdentity(User user) {
    UserIdentityId userIdentityId = new UserIdentityId();
    userIdentityId.setUserId(user.getId());
    userIdentityId.setProvider(PROVIDER);
    userIdentityId.setProviderUserId(GOOGLE_ID);

    return new UserIdentity(
        userIdentityId,
        user
    );
  }
}
