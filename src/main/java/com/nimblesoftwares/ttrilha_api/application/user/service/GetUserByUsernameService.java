package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserByUsernameUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

public class GetUserByUsernameService implements GetUserByUsernameUseCase {

  private final UserRepositoryPort userRepository;

  public GetUserByUsernameService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User execute(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found."));
  }
}
