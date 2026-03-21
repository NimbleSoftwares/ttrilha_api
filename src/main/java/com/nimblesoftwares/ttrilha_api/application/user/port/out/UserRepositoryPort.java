package com.nimblesoftwares.ttrilha_api.application.user.port.out;

import com.nimblesoftwares.ttrilha_api.domain.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

   User save(User user);
   Optional<User> findById(UUID id);
   Optional<User> findByUsername(String username);
}
