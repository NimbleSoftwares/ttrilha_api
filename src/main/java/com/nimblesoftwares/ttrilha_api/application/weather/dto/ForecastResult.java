package com.nimblesoftwares.ttrilha_api.application.weather.dto;

import java.util.List;

public record ForecastResult(
    String city,
    String date,
    List<ForecastEntry> entries
) {
  public record ForecastEntry(
      String time,
      double temp,
      double feelsLike,
      int humidity,
      String description,
      double windSpeed
  ) {}
}

