package com.nimblesoftwares.ttrilha_api.application.user.port.out;

import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;

import java.util.List;
import java.util.UUID;

public interface FriendshipRepositoryPort {

  Friendship save(Friendship friendship);

  List<Friendship> findByUserId(UUID userId);

  void deleteByBothSides(UUID userA, UUID userB);
}
