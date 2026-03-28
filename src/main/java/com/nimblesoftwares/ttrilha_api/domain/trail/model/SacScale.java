package com.nimblesoftwares.ttrilha_api.domain.trail.model;

public enum SacScale {
  UNKNOWN,
  EASY,
  MODERATE,
  HARD,
  VERY_HARD,
  EXTREME,
  TECHNICAL;

  public static SacScale fromTag(String value) {
    return switch (value) {
      case "hiking" -> SacScale.EASY;
      case "mountain_hiking" -> SacScale.MODERATE;
      case "demanding_mountain_hiking" -> SacScale.HARD;
      case "alpine_hiking" -> SacScale.VERY_HARD;
      case "demanding_alpine_hiking" -> SacScale.EXTREME;
      case "difficult_alpine_hiking" -> SacScale.TECHNICAL;
      default -> UNKNOWN;
    };
  }
}
