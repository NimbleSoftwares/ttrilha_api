package com.nimblesoftwares.ttrilha_api.application.trail.command;

import java.math.BigDecimal;

public record ExploreTrailCommand(
  BigDecimal lat,
  BigDecimal lon,
  String locationName,
  Integer radiusKm
) { }
