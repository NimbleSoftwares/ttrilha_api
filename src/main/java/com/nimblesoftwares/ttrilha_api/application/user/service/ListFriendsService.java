package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListFriendsUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;

import java.util.List;
import java.util.UUID;

public class ListFriendsService implements ListFriendsUseCase {

  private final FriendshipRepositoryPort friendshipRepository;

  public ListFriendsService(FriendshipRepositoryPort friendshipRepository) {
    this.friendshipRepository = friendshipRepository;
  }

  @Override
  public List<Friendship> execute(UUID userId) {
    return friendshipRepository.findByUserId(userId);
  }
}
