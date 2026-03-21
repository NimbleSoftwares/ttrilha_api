package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.block.entities.UserBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserBlockJpaRepository extends JpaRepository<UserBlockEntity, UUID> {

  boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

  void deleteByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);
}

