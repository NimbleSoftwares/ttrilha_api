package com.nimblesoftwares.ttrilha_api.application.weather.port.out;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;

import java.time.LocalDate;

public interface WeatherPort {

  CurrentWeatherResult getCurrentWeatherByCity(String city);

  ForecastResult getForecastByCoordinates(double lat, double lon, LocalDate date);
}

