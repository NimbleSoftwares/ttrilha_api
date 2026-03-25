package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.LineStringMapper;
import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionDetailResult;
import com.nimblesoftwares.ttrilha_api.application.expedition.dto.MemberInfo;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.GetExpeditionUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.exception.ExpeditionNotFoundException;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.List;
import java.util.UUID;

public class GetExpeditionService implements GetExpeditionUseCase {

  private final ExpeditionRepositoryPort expeditionRepository;
  private final TrailRepositoryPort trailRepository;
  private final UserRepositoryPort userRepository;
  private final LineStringMapper lineStringMapper;

  public GetExpeditionService(ExpeditionRepositoryPort expeditionRepository,
                               TrailRepositoryPort trailRepository,
                               UserRepositoryPort userRepository,
                               LineStringMapper lineStringMapper) {
    this.expeditionRepository = expeditionRepository;
    this.trailRepository = trailRepository;
    this.userRepository = userRepository;
    this.lineStringMapper = lineStringMapper;
  }

  @Override
  public ExpeditionDetailResult execute(UUID expeditionId, UUID currentUserId) {
    Expedition expedition = expeditionRepository.findById(expeditionId)
        .orElseThrow(() -> new ExpeditionNotFoundException("Expedition not found: " + expeditionId));

    // Only ACCEPTED members can access detail
    boolean isAcceptedMember = expedition.getMembers().stream()
        .anyMatch(m -> m.getUserId().equals(currentUserId) && m.getStatus() == MemberStatus.ACCEPTED);
    if (!isAcceptedMember) {
      throw new ExpeditionNotFoundException("Expedition not found: " + expeditionId);
    }

    Trail trail = trailRepository.findByOsmId(expedition.getOsmId())
        .orElseThrow(() -> new ExpeditionNotFoundException("Trail not found for expedition: " + expeditionId));

    List<GeoPoint> geometry = lineStringMapper.toGeoPoints(trail.getGeometry());

    // Return ACCEPTED and PENDING members, exclude REJECTED/REMOVED
    List<MemberInfo> members = expedition.getMembers().stream()
        .filter(m -> m.getStatus() == MemberStatus.ACCEPTED || m.getStatus() == MemberStatus.PENDING)
        .map(m -> userRepository.findById(m.getUserId())
            .map(u -> new MemberInfo(m.getInviteId(), u.getId(), u.getDisplayName(), u.getAvatarUrl(), m.getStatus()))
            .orElse(new MemberInfo(m.getInviteId(), m.getUserId(), "Unknown", null, m.getStatus())))
        .toList();

    return new ExpeditionDetailResult(
        expedition.getId(),
        expedition.getTitle(),
        expedition.getOsmId(),
        trail.getName(),
        expedition.getStartDate(),
        expedition.getEndDate(),
        expedition.getStatus(),
        expedition.getCreatedByUserId(),
        members,
        geometry,
        expedition.getCreatedAt()
    );
  }
}
