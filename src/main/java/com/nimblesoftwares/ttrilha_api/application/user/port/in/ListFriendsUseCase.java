package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.application.user.dto.FriendResult;

import java.util.List;
import java.util.UUID;

public interface ListFriendsUseCase {

  List<FriendResult> execute(UUID userId);
}
