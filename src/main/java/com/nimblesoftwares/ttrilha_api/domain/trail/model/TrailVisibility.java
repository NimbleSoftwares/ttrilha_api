package com.nimblesoftwares.ttrilha_api.domain.trail.model;

public enum TrailVisibility {
  UNKNOWN,
  NO,
  BAD,
  HORRIBLE,
  INTERMEDIATE,
  GOOD,
  EXCELLENT;

  public static TrailVisibility fromTag(String value) {
    return switch (value) {
      case "no" -> TrailVisibility.NO;
      case "bad" -> TrailVisibility.BAD;
      case "horrible" -> TrailVisibility.HORRIBLE;
      case "intermediate" -> TrailVisibility.INTERMEDIATE;
      case "good" -> TrailVisibility.GOOD;
      case "excellent" -> TrailVisibility.EXCELLENT;
      default -> UNKNOWN;
    };
  }
}
