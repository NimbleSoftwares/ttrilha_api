package com.nimblesoftwares.ttrilha_api.application.user.port.out;

import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

public interface UserRepositoryPort {

  public User save(User user);
}
