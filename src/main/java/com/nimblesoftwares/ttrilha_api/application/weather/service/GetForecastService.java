package com.nimblesoftwares.ttrilha_api.application.weather.service;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetForecastUseCase;
import com.nimblesoftwares.ttrilha_api.application.weather.port.out.WeatherPort;

import java.time.LocalDate;

public class GetForecastService implements GetForecastUseCase {

  private final WeatherPort weatherPort;

  public GetForecastService(WeatherPort weatherPort) {
    this.weatherPort = weatherPort;
  }

  @Override
  public ForecastResult execute(double lat, double lon, LocalDate date) {
    return weatherPort.getForecastByCoordinates(lat, lon, date);
  }
}

