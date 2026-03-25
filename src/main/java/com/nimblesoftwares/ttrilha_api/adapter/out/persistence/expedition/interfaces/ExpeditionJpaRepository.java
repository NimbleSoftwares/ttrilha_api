package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.entities.ExpeditionEntity;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpeditionJpaRepository extends JpaRepository<ExpeditionEntity, UUID> {

  // All expeditions where user has ACCEPTED status
  @Query("SELECT DISTINCT e FROM ExpeditionEntity e JOIN e.members m WHERE m.userId = :userId AND m.status = 'ACCEPTED'")
  List<ExpeditionEntity> findAllByMemberWithAcceptedStatus(@Param("userId") UUID userId);

  // Same but also filter by expedition status
  @Query("SELECT DISTINCT e FROM ExpeditionEntity e JOIN e.members m WHERE m.userId = :userId AND m.status = 'ACCEPTED' AND e.status = :expeditionStatus")
  List<ExpeditionEntity> findAllByMemberWithAcceptedStatusAndExpeditionStatus(
      @Param("userId") UUID userId,
      @Param("expeditionStatus") ExpeditionStatus expeditionStatus);

  // Find expedition by invite_id inside members
  @Query("SELECT DISTINCT e FROM ExpeditionEntity e JOIN e.members m WHERE m.inviteId = :inviteId")
  Optional<ExpeditionEntity> findByInviteId(@Param("inviteId") UUID inviteId);

  // Add a member row
  @Modifying
  @Query(value = "INSERT INTO expedition_members (expedition_id, invite_id, user_id, status) VALUES (:expeditionId, gen_random_uuid(), :userId, :#{#status.name()})", nativeQuery = true)
  void addMember(@Param("expeditionId") UUID expeditionId,
                 @Param("userId") UUID userId,
                 @Param("status") MemberStatus status);

  // Update member status by invite_id
  @Modifying
  @Query(value = "UPDATE expedition_members SET status = :#{#status.name()} WHERE invite_id = :inviteId", nativeQuery = true)
  void updateMemberStatus(@Param("inviteId") UUID inviteId,
                          @Param("status") MemberStatus status);

  // Count how many times an invitee was invited to an expedition (limit 3)
  @Query(value = "SELECT COUNT(*) FROM expedition_members WHERE expedition_id = :expeditionId AND user_id = :inviteeId", nativeQuery = true)
  int countInvitesByExpeditionAndInvitee(@Param("expeditionId") UUID expeditionId,
                                         @Param("inviteeId") UUID inviteeId);

  // Soft-delete: remove owner from members + set expedition CANCELLED
  @Modifying
  @Query(value = "UPDATE expedition_members SET status = 'REMOVED' WHERE expedition_id = :expeditionId AND user_id = :ownerId", nativeQuery = true)
  void removeOwnerFromMembers(@Param("expeditionId") UUID expeditionId, @Param("ownerId") UUID ownerId);

  @Modifying
  @Query("UPDATE ExpeditionEntity e SET e.status = 'CANCELLED' WHERE e.id = :expeditionId")
  void cancelExpedition(@Param("expeditionId") UUID expeditionId);

  // All expeditions where user has a PENDING member row (for pending invites)
  @Query("SELECT DISTINCT e FROM ExpeditionEntity e JOIN e.members m WHERE m.userId = :userId AND m.status = 'PENDING'")
  List<ExpeditionEntity> findAllByPendingMember(@Param("userId") UUID userId);
}
