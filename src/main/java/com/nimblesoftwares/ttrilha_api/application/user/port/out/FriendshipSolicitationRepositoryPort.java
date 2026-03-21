package com.nimblesoftwares.ttrilha_api.application.user.port.out;

import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipSolicitation;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipSolicitationRepositoryPort {

  FriendshipSolicitation save(FriendshipSolicitation solicitation);

  Optional<FriendshipSolicitation> findById(UUID id);

  boolean existsPendingBetween(UUID requesterId, UUID addresseeId);

  List<FriendshipSolicitation> findPendingByAddresseeId(UUID addresseeId);

  void cancelPendingBetween(UUID userA, UUID userB);
}
