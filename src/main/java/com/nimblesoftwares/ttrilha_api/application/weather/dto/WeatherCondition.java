package com.nimblesoftwares.ttrilha_api.application.weather.dto;

/**
 * Weather condition groups based on OpenWeatherMap condition codes.
 * Reference: https://openweathermap.org/weather-conditions
 *
 * Use this enum on mobile to select the correct icon.
 *
 * Ranges:
 *  2xx → THUNDERSTORM
 *  3xx → DRIZZLE
 *  5xx → RAIN
 *  6xx → SNOW
 *  7xx → ATMOSPHERE (fog, mist, haze, etc.)
 *  800 → CLEAR
 *  80x → CLOUDS
 */
public enum WeatherCondition {

  THUNDERSTORM,   // 200–232
  DRIZZLE,        // 300–321
  RAIN,           // 500–531
  SNOW,           // 600–622
  ATMOSPHERE,     // 701–781 (mist, smoke, haze, fog, tornado…)
  CLEAR,          // 800
  CLOUDS;         // 801–804

  public static WeatherCondition fromConditionId(int id) {
    if (id >= 200 && id < 300) return THUNDERSTORM;
    if (id >= 300 && id < 400) return DRIZZLE;
    if (id >= 500 && id < 600) return RAIN;
    if (id >= 600 && id < 700) return SNOW;
    if (id >= 700 && id < 800) return ATMOSPHERE;
    if (id == 800)             return CLEAR;
    return CLOUDS;
  }
}

