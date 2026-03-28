package com.nimblesoftwares.ttrilha_api.application.weather.exception;

import com.nimblesoftwares.ttrilha_api.application.ApplicationException;

public class WeatherCityNotFoundException extends ApplicationException {

  public WeatherCityNotFoundException(String city) {
    super("City not found or weather unavailable: " + city);
  }
}

