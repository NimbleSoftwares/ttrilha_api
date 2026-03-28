package com.nimblesoftwares.ttrilha_api.application.weather.port.in;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;

import java.util.UUID;

public interface GetCurrentWeatherUseCase {

  CurrentWeatherResult execute(UUID userId);
}
