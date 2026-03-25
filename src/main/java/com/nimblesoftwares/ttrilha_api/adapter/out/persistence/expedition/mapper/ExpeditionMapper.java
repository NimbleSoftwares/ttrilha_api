package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.mapper;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.entities.ExpeditionEntity;
import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.expedition.entities.ExpeditionMemberEntity;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.Expedition;
import com.nimblesoftwares.ttrilha_api.domain.expedition.model.ExpeditionMember;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpeditionMapper {

  public ExpeditionEntity toPersistence(Expedition expedition) {
    ExpeditionEntity entity = new ExpeditionEntity();
    entity.setTitle(expedition.getTitle());
    entity.setOsmId(expedition.getOsmId());
    entity.setStartDate(expedition.getStartDate());
    entity.setEndDate(expedition.getEndDate());
    entity.setStatus(expedition.getStatus());
    entity.setCreatedByUserId(expedition.getCreatedByUserId());
    entity.setMembers(toMemberEntities(expedition.getMembers()));
    return entity;
  }

  public Expedition toDomain(ExpeditionEntity entity) {
    Expedition expedition = new Expedition();
    expedition.setId(entity.getId());
    expedition.setTitle(entity.getTitle());
    expedition.setOsmId(entity.getOsmId());
    expedition.setStartDate(entity.getStartDate());
    expedition.setEndDate(entity.getEndDate());
    expedition.setStatus(entity.getStatus());
    expedition.setCreatedByUserId(entity.getCreatedByUserId());
    expedition.setMembers(toMemberDomains(entity.getMembers()));
    expedition.setCreatedAt(entity.getCreatedAt());
    return expedition;
  }

  private List<ExpeditionMemberEntity> toMemberEntities(List<ExpeditionMember> members) {
    if (members == null) return List.of();
    return members.stream()
        .map(m -> new ExpeditionMemberEntity(m.getInviteId(), m.getUserId(), m.getStatus()))
        .toList();
  }

  private List<ExpeditionMember> toMemberDomains(List<ExpeditionMemberEntity> entities) {
    if (entities == null) return List.of();
    return entities.stream()
        .map(e -> new ExpeditionMember(e.getInviteId(), e.getUserId(), e.getStatus()))
        .toList();
  }
}
