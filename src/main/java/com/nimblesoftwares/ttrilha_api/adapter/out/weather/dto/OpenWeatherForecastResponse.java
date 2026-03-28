package com.nimblesoftwares.ttrilha_api.adapter.out.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Mapped response from OpenWeatherMap /data/2.5/forecast (5-day / 3-hour forecast).
 */
public record OpenWeatherForecastResponse(
    City city,
    List<ForecastItem> list
) {

  public record City(
      String name
  ) {}

  public record ForecastItem(
      Main main,
      List<Weather> weather,
      Wind wind,
      @JsonProperty("dt_txt") String dtTxt
  ) {}

  public record Main(
      double temp,
      @JsonProperty("feels_like") double feelsLike,
      int humidity
  ) {}

  public record Weather(
      String description
  ) {}

  public record Wind(
      double speed
  ) {}
}

