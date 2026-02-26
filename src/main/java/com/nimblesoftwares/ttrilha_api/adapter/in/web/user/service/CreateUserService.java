package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.CreateUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.CreateUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateUserService implements CreateUserUseCase {

  private final UserRepositoryPort userRepository;

  public CreateUserService(UserRepositoryPort userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UUID execute(CreateUserCommand command) {
    User saved = userRepository.save(command.toUser());
    return saved.getId();
  }
}
