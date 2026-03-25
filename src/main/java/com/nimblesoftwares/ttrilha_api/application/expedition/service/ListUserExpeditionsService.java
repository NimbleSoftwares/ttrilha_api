package com.nimblesoftwares.ttrilha_api.application.expedition.service;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionSummaryResult;
import com.nimblesoftwares.ttrilha_api.application.expedition.dto.MemberInfo;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.in.ListUserExpeditionsUseCase;
import com.nimblesoftwares.ttrilha_api.application.expedition.port.out.ExpeditionRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ListUserExpeditionsService implements ListUserExpeditionsUseCase {

  private final ExpeditionRepositoryPort expeditionRepository;
  private final TrailRepositoryPort trailRepository;
  private final UserRepositoryPort userRepository;

  public ListUserExpeditionsService(ExpeditionRepositoryPort expeditionRepository,
                                     TrailRepositoryPort trailRepository,
                                     UserRepositoryPort userRepository) {
    this.expeditionRepository = expeditionRepository;
    this.trailRepository = trailRepository;
    this.userRepository = userRepository;
  }

  @Override
  public List<ExpeditionSummaryResult> execute(UUID userId, Optional<ExpeditionStatus> status, String sort) {
    List<Expedition> expeditions = status.isPresent()
        ? expeditionRepository.findAllByMemberWithAcceptedStatusAndExpeditionStatus(userId, status.get())
        : expeditionRepository.findAllByMemberWithAcceptedStatus(userId);

    boolean descending = "DESC".equalsIgnoreCase(sort);

    // Without status filter → sort by startDate; with status filter → sort by endDate
    Comparator<Expedition> comparator = status.isPresent()
        ? Comparator.comparing(Expedition::getEndDate)
        : Comparator.comparing(Expedition::getStartDate);

    if (descending) comparator = comparator.reversed();

    return expeditions.stream()
        .sorted(comparator)
        .map(expedition -> {
          Trail trail = trailRepository.findByOsmId(expedition.getOsmId()).orElse(null);
          String trailName = trail != null ? trail.getName() : "Unknown trail";
          Double distanceMeters = trail != null ? trail.getDistanceMeters() : null;

          List<MemberInfo> members = expedition.getMembers().stream()
              .filter(m -> m.getStatus() == MemberStatus.ACCEPTED || m.getStatus() == MemberStatus.PENDING)
              .map(m -> userRepository.findById(m.getUserId())
                  .map(u -> new MemberInfo(m.getInviteId(), u.getId(), u.getDisplayName(), u.getAvatarUrl(), m.getStatus()))
                  .orElse(new MemberInfo(m.getInviteId(), m.getUserId(), "Unknown", null, m.getStatus())))
              .toList();

          return new ExpeditionSummaryResult(
              expedition.getId(),
              expedition.getTitle(),
              trailName,
              distanceMeters,
              expedition.getStartDate(),
              expedition.getEndDate(),
              members
          );
        })
        .toList();
  }
}
