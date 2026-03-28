package com.nimblesoftwares.ttrilha_api.application.weather.port.in;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;

import java.time.LocalDate;

public interface GetForecastUseCase {

  ForecastResult execute(double lat, double lon, LocalDate date);
}

