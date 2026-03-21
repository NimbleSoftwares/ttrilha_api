package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.friendship.entities.FriendshipSolicitationEntity;
import com.nimblesoftwares.ttrilha_api.domain.user.model.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipSolicitationJpaRepository extends JpaRepository<FriendshipSolicitationEntity, UUID> {

  boolean existsByRequesterIdAndAddresseeIdAndStatus(UUID requesterId, UUID addresseeId, FriendshipStatus status);

  List<FriendshipSolicitationEntity> findByAddresseeIdAndStatus(UUID addresseeId, FriendshipStatus status);

  @Modifying
  @Query("UPDATE FriendshipSolicitationEntity s SET s.status = 'CANCELLED' " +
      "WHERE s.status = 'PENDING' AND " +
      "((s.requesterId = :userA AND s.addresseeId = :userB) OR (s.requesterId = :userB AND s.addresseeId = :userA))")
  void cancelPendingBetween(@Param("userA") UUID userA, @Param("userB") UUID userB);
}
