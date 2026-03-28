package com.nimblesoftwares.ttrilha_api.application.weather.service;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetCurrentWeatherUseCase;
import com.nimblesoftwares.ttrilha_api.application.weather.port.out.WeatherPort;

public class GetCurrentWeatherService implements GetCurrentWeatherUseCase {

  private final WeatherPort weatherPort;

  public GetCurrentWeatherService(WeatherPort weatherPort) {
    this.weatherPort = weatherPort;
  }

  @Override
  public CurrentWeatherResult execute(String city) {
    return weatherPort.getCurrentWeatherByCity(city);
  }
}

