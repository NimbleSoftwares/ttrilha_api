package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.RespondFriendshipInviteCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.RespondFriendshipInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipSolicitationRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.FriendshipSolicitationNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipSolicitation;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;
import jakarta.transaction.Transactional;

@Transactional
public class RespondFriendshipInviteService implements RespondFriendshipInviteUseCase {

  private final FriendshipSolicitationRepositoryPort solicitationRepository;
  private final FriendshipRepositoryPort friendshipRepository;

  public RespondFriendshipInviteService(
      FriendshipSolicitationRepositoryPort solicitationRepository,
      FriendshipRepositoryPort friendshipRepository) {
    this.solicitationRepository = solicitationRepository;
    this.friendshipRepository = friendshipRepository;
  }

  @Override
  public void execute(RespondFriendshipInviteCommand command) {
    FriendshipSolicitation solicitation = solicitationRepository.findById(command.solicitationId())
        .orElseThrow(() -> new FriendshipSolicitationNotFoundException("Solicitation not found."));

    if (!solicitation.getAddresseeId().equals(command.responderId())) {
      throw new FriendshipSolicitationNotFoundException("Solicitation not found.");
    }

    solicitation.setStatus(command.status());
    solicitationRepository.save(solicitation);

    if (command.status() == FriendshipStatus.ACCEPTED) {
      friendshipRepository.save(new Friendship(solicitation.getRequesterId(), solicitation.getAddresseeId(), solicitation.getId()));
      friendshipRepository.save(new Friendship(solicitation.getAddresseeId(), solicitation.getRequesterId(), solicitation.getId()));
    }
  }
}
