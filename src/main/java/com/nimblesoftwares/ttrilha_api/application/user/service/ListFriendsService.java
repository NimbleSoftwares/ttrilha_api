package com.nimblesoftwares.ttrilha_api.application.user.service;

import com.nimblesoftwares.ttrilha_api.application.user.dto.FriendResult;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.ListFriendsUseCase;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.FriendshipRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.UserNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

import java.util.List;
import java.util.UUID;

public class ListFriendsService implements ListFriendsUseCase {

  private final FriendshipRepositoryPort friendshipRepository;
  private final UserRepositoryPort userRepository;

  public ListFriendsService(FriendshipRepositoryPort friendshipRepository, UserRepositoryPort userRepository) {
    this.friendshipRepository = friendshipRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<FriendResult> execute(UUID userId) {
    return friendshipRepository.findByUserId(userId).stream()
        .map(friendship -> {
          User friend = userRepository.findById(friendship.getFriendId())
              .orElseThrow(() -> new UserNotFoundException("Friend not found: " + friendship.getFriendId()));
          return new FriendResult(
              friend.getId(),
              friend.getUsername(),
              friend.getDisplayName(),
              friend.getFirstName(),
              friend.getLastName(),
              friend.getAvatarUrl(),
              friend.getCity(),
              friend.getState(),
              friendship.getCreatedAt()
          );
        })
        .toList();
  }
}
