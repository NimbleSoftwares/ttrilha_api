package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionSummaryResult;
import com.nimblesoftwares.ttrilha_api.application.expedition.dto.MemberInfo;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ExpeditionSummaryResponse(
    UUID id,
    String title,
    String trailName,
    Double distanceMeters,
    LocalDate startDate,
    LocalDate endDate,
    int membersCount,
    List<MemberSummary> members
) {
  public record MemberSummary(UUID inviteId, UUID id, String displayName, String avatarUrl, MemberStatus memberStatus) {
    public static MemberSummary from(MemberInfo m) {
      return new MemberSummary(m.inviteId(), m.id(), m.displayName(), m.avatarUrl(), m.status());
    }
  }

  public static ExpeditionSummaryResponse fromResult(ExpeditionSummaryResult result) {
    List<MemberSummary> members = result.members().stream()
        .map(MemberSummary::from)
        .toList();

    return new ExpeditionSummaryResponse(
        result.id(),
        result.title(),
        result.trailName(),
        result.distanceMeters(),
        result.startDate(),
        result.endDate(),
        members.size(),
        members
    );
  }
}
