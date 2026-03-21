package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipJpaRepository extends JpaRepository<FriendshipEntity, UUID> {

  List<FriendshipEntity> findByUserId(UUID userId);

  @Modifying
  @Query("DELETE FROM FriendshipEntity f WHERE (f.userId = :userA AND f.friendId = :userB) OR (f.userId = :userB AND f.friendId = :userA)")
  void deleteByBothSides(@Param("userA") UUID userA, @Param("userB") UUID userB);
}
