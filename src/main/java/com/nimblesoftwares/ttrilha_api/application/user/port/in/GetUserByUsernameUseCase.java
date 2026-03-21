package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

public interface GetUserByUsernameUseCase {

  User execute(String username);
}
