package com.nimblesoftwares.ttrilha_api.adapter.in.web.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.CreateUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.CreateUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CreateUserService implements CreateUserUseCase {

  private final UserRepositoryPort userRepository;
  private final UserIdentityRepositoryPort userIdentityRepository;

  public CreateUserService(
      UserIdentityRepositoryPort userIdentityRepository, UserRepositoryPort userRepository) {
    this.userIdentityRepository = userIdentityRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UUID execute(CreateUserCommand command) {

    Optional<UserIdentity> existing = userIdentityRepository.findByProviderAndProviderUserId(
        command.provider(),
        command.providerUserId()
    );

    if(existing.isPresent()) return existing.get().getUser().getId();

    User savedUser = userRepository.save(command.toUser());

    userIdentityRepository.save(command.toUserIdentity(savedUser));

    return savedUser.getId();
  }
}
