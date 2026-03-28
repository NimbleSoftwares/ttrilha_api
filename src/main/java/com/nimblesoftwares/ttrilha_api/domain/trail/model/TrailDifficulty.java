package com.nimblesoftwares.ttrilha_api.domain.trail.model;

public enum TrailDifficulty {
  UNKNOWN,
  EASY,
  MODERATE,
  HARD,
  VERY_HARD,
  EXTREME,
  TECHNICAL;

  /**
   * Calcula a dificuldade real da trilha combinando:
   * - SacScale (dificuldade técnica)
   * - TrailVisibility (facilidade de navegação)
   *
   * Estratégia:
   * - SacScale tem peso maior (70%)
   * - TrailVisibility influencia risco de se perder (30%)
   *
   * @param sacScale nível técnico da trilha
   * @param trailVisibility visibilidade do caminho
   * @return dificuldade consolidada da trilha
   */
  public static TrailDifficulty calculateDifficulty(
      SacScale sacScale,
      TrailVisibility trailVisibility
  ) {

    int sacScore = sacScaleToScore(sacScale);
    int visScore = visibilityToScore(trailVisibility);

    if (sacScore == 0 && visScore == 0) {
      return UNKNOWN;
    }

    double weightedScore = (sacScore * 0.7) + (visScore * 0.3);

    return mapScoreToDifficulty(weightedScore);
  }

  private static int sacScaleToScore(SacScale sac) {
    return switch (sac) {
      case EASY -> 1;
      case MODERATE -> 2;
      case HARD -> 3;
      case VERY_HARD -> 4;
      case EXTREME -> 5;
      case TECHNICAL -> 6;
      default -> 0;
    };
  }

  private static int visibilityToScore(TrailVisibility vis) {
    return switch (vis) {
      case EXCELLENT -> 1;
      case GOOD -> 2;
      case INTERMEDIATE -> 3;
      case BAD -> 4;
      case HORRIBLE -> 5;
      case NO -> 6;
      default -> 0;
    };
  }

  private static TrailDifficulty mapScoreToDifficulty(double score) {
    if (score <= 1.5) return EASY;
    if (score <= 2.5) return MODERATE;
    if (score <= 3.5) return HARD;
    if (score <= 4.5) return VERY_HARD;
    if (score <= 5.5) return EXTREME;
    return TECHNICAL;
  }
}
