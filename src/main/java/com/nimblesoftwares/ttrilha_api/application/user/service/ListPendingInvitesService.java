package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.dto.PendingInviteResult;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListPendingInvitesUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipSolicitationRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipSolicitation;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

import java.util.List;
import java.util.UUID;

public class ListPendingInvitesService implements ListPendingInvitesUseCase {

  private final FriendshipSolicitationRepositoryPort solicitationRepository;
  private final UserRepositoryPort userRepository;

  public ListPendingInvitesService(
      FriendshipSolicitationRepositoryPort solicitationRepository,
      UserRepositoryPort userRepository) {
    this.solicitationRepository = solicitationRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<PendingInviteResult> execute(UUID addresseeId) {
    List<FriendshipSolicitation> solicitations = solicitationRepository.findPendingByAddresseeId(addresseeId);

    return solicitations.stream()
        .map(solicitation -> {
          User requester = userRepository.findById(solicitation.getRequesterId())
              .orElseThrow(() -> new UserNotFoundException(
                  "Requester user not found: " + solicitation.getRequesterId()));

          return new PendingInviteResult(
              solicitation.getId(),
              requester.getId(),
              requester.getDisplayName(),
              requester.getFirstName(),
              requester.getLastName(),
              requester.getAvatarUrl(),
              solicitation.getCreatedAt()
          );
        })
        .toList();
  }
}
