package com.nimblesoftwares.ttrilha_api.application.weather.port.in;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;

public interface GetCurrentWeatherUseCase {

  CurrentWeatherResult execute(String city);
}

