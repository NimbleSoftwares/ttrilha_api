package com.nimblesoftwares.ttrilha_api.application.weather.service;

import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.exception.WeatherCityNotFoundException;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetCurrentWeatherUseCase;
import com.nimblesoftwares.ttrilha_api.application.weather.port.out.WeatherPort;

import java.util.UUID;

public class GetCurrentWeatherService implements GetCurrentWeatherUseCase {

  private final WeatherPort weatherPort;
  private final UserRepositoryPort userRepositoryPort;

  public GetCurrentWeatherService(WeatherPort weatherPort, UserRepositoryPort userRepositoryPort) {
    this.weatherPort = weatherPort;
    this.userRepositoryPort = userRepositoryPort;
  }

  @Override
  public CurrentWeatherResult execute(UUID userId) {
    String city = userRepositoryPort.findById(userId)
        .map(user -> user.getCity())
        .filter(c -> c != null && !c.isBlank())
        .orElseThrow(() -> new WeatherCityNotFoundException("User has no city configured in their profile."));

    return weatherPort.getCurrentWeatherByCity(city);
  }
}
