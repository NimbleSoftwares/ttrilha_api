package com.nimblesoftwares.ttrilha_api.adapter.in.web.expedition.dto;

import com.nimblesoftwares.ttrilha_api.application.expedition.dto.ExpeditionDetailResult;
import com.nimblesoftwares.ttrilha_api.application.expedition.dto.MemberInfo;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionStatus;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.MemberStatus;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ExpeditionDetailResponse(
    UUID id,
    String title,
    Long osmId,
    String trailName,
    LocalDate startDate,
    LocalDate endDate,
    ExpeditionStatus status,
    UUID createdByUserId,
    List<MemberDetail> members,
    List<GeoPoint> geometry,
    OffsetDateTime createdAt
) {
  public record MemberDetail(UUID inviteId, UUID id, String displayName, String avatarUrl, MemberStatus memberStatus) {
    public static MemberDetail from(MemberInfo m) {
      return new MemberDetail(m.inviteId(), m.id(), m.displayName(), m.avatarUrl(), m.status());
    }
  }

  public static ExpeditionDetailResponse fromResult(ExpeditionDetailResult result) {
    List<MemberDetail> members = result.members().stream()
        .map(MemberDetail::from)
        .toList();

    return new ExpeditionDetailResponse(
        result.id(),
        result.title(),
        result.osmId(),
        result.trailName(),
        result.startDate(),
        result.endDate(),
        result.status(),
        result.createdByUserId(),
        members,
        result.geometry(),
        result.createdAt()
    );
  }
}
