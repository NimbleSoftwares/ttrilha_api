package com.nimblesoftwares.ttrilha_api.application.trail.dto;

import com.nimblesoftwares.ttrilha_api.domain.trail.model.Trail;

import java.util.List;

public record ExploreTrailResult(List<Trail> trails) {

  public static ExploreTrailResult from(List<Trail> trails) {
    return new ExploreTrailResult(trails);
  }
}
