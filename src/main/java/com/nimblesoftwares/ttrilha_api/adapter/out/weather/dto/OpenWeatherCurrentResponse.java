package com.nimblesoftwares.ttrilha_api.adapter.out.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Mapped response from OpenWeatherMap /data/2.5/weather (current weather).
 */
public record OpenWeatherCurrentResponse(
    String name,
    Main main,
    List<Weather> weather,
    Wind wind
) {

  public record Main(
      double temp,
      @JsonProperty("feels_like") double feelsLike,
      int humidity
  ) {}

  public record Weather(
      int id,
      String description
  ) {}

  public record Wind(
      double speed
  ) {}
}
