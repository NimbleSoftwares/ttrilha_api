package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.command.SendFriendshipInviteCommand;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.SendFriendshipInviteUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipSolicitationRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserBlockRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.CannotSendInviteToSelfException;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserBlockedException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipSolicitation;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Transactional
public class SendFriendshipInviteService implements SendFriendshipInviteUseCase {

  private final FriendshipSolicitationRepositoryPort solicitationRepository;
  private final UserBlockRepositoryPort userBlockRepository;

  public SendFriendshipInviteService(
      FriendshipSolicitationRepositoryPort solicitationRepository,
      UserBlockRepositoryPort userBlockRepository) {
    this.solicitationRepository = solicitationRepository;
    this.userBlockRepository = userBlockRepository;
  }

  @Override
  public UUID execute(SendFriendshipInviteCommand command) {
    if (command.requesterId().equals(command.addresseeId())) {
      throw new CannotSendInviteToSelfException("You cannot send a friendship invite to yourself.");
    }

    // Check block in both directions
    if (userBlockRepository.existsBlock(command.requesterId(), command.addresseeId()) ||
        userBlockRepository.existsBlock(command.addresseeId(), command.requesterId())) {
      throw new UserBlockedException("Cannot send a friendship invite: a block exists between these users.");
    }

    // Enviar notificacao push
    FriendshipSolicitation solicitation = new FriendshipSolicitation(command.requesterId(), command.addresseeId());
    FriendshipSolicitation saved = solicitationRepository.save(solicitation);
    return saved.getId();
  }
}
