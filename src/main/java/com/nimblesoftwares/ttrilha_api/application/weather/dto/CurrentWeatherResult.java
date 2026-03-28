package com.nimblesoftwares.ttrilha_api.application.weather.dto;

public record CurrentWeatherResult(
    String city,
    double temp,
    double feelsLike,
    int humidity,
    String description,
    int conditionId,
    WeatherCondition condition,
    double windSpeed
) {}
