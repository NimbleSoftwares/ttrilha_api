package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.BlockUserCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.BlockUserUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipSolicitationRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserBlockRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.CannotBlockSelfException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.UserBlock;
import jakarta.transaction.Transactional;

@Transactional
public class BlockUserService implements BlockUserUseCase {

  private final UserBlockRepositoryPort userBlockRepository;
  private final FriendshipRepositoryPort friendshipRepository;
  private final FriendshipSolicitationRepositoryPort solicitationRepository;

  public BlockUserService(
      UserBlockRepositoryPort userBlockRepository,
      FriendshipRepositoryPort friendshipRepository,
      FriendshipSolicitationRepositoryPort solicitationRepository) {
    this.userBlockRepository = userBlockRepository;
    this.friendshipRepository = friendshipRepository;
    this.solicitationRepository = solicitationRepository;
  }

  @Override
  public void execute(BlockUserCommand command) {
    if (command.blockerId().equals(command.blockedId())) {
      throw new CannotBlockSelfException("You cannot block yourself.");
    }

    // Cancel any pending solicitations in both directions
    solicitationRepository.cancelPendingBetween(command.blockerId(), command.blockedId());

    // Remove friendship in both directions (physical delete — traceability via user_blocks)
    friendshipRepository.deleteByBothSides(command.blockerId(), command.blockedId());

    // Save the block (throws UserAlreadyBlockedException if duplicate)
    userBlockRepository.save(new UserBlock(command.blockerId(), command.blockedId()));
  }
}

