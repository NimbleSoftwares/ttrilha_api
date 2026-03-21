package com.nimblesoftwares.ttrilha_api.application.user.port.out;

import com.nimblesoftwares.ttrilha_api.domain.user.model.UserBlock;

import java.util.UUID;

public interface UserBlockRepositoryPort {

  UserBlock save(UserBlock block);

  boolean existsBlock(UUID blockerId, UUID blockedId);

  void delete(UUID blockerId, UUID blockedId);
}

