package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.SaveUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SaveUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserIdentity;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
public class SaveUserService implements SaveUserUseCase {

  private final UserRepositoryPort userRepository;
  private final UserIdentityRepositoryPort userIdentityRepository;

  public SaveUserService(
      UserIdentityRepositoryPort userIdentityRepository, UserRepositoryPort userRepository) {
    this.userIdentityRepository = userIdentityRepository;
    this.userRepository = userRepository;
  }

  @Override
  public UUID execute(SaveUserCommand command) {

    Optional<UserIdentity> existingOpt = userIdentityRepository.findByProviderAndProviderUserId(
        command.provider(),
        command.providerUserId()
    );

    if(existingOpt.isPresent()) {
      User userToUpdate = command.toUser();
      userToUpdate.setId(existingOpt.get().getId().getUserId());
      User updatedUser = userRepository.save(userToUpdate);
      return updatedUser.getId();
    }

    User savedUser = userRepository.save(command.toUser());
    userIdentityRepository.save(command.toUserIdentity(savedUser));

    return savedUser.getId();
  }
}
