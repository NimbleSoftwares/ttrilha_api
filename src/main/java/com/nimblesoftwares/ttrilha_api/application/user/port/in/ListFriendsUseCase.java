package com.nimblesoftwares.ttrilha_api.application.user.port.in;

import com.nimblesoftwares.ttrilha_api.domain.user.model.Friendship;

import java.util.List;
import java.util.UUID;

public interface ListFriendsUseCase {

  List<Friendship> execute(UUID userId);
}
