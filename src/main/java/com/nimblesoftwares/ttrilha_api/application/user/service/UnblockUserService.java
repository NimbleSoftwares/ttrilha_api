package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.UnblockUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.UnblockUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserBlockRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserBlockNotFoundException;
import jakarta.transaction.Transactional;

@Transactional
public class UnblockUserService implements UnblockUserUseCase {

  private final UserBlockRepositoryPort userBlockRepository;

  public UnblockUserService(UserBlockRepositoryPort userBlockRepository) {
    this.userBlockRepository = userBlockRepository;
  }

  @Override
  public void execute(UnblockUserCommand command) {
    if (!userBlockRepository.existsBlock(command.blockerId(), command.blockedId())) {
      throw new UserBlockNotFoundException("No block found for the given user.");
    }
    userBlockRepository.delete(command.blockerId(), command.blockedId());
  }
}

