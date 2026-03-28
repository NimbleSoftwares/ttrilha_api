package com.nimblesoftwares.ttrilha_api.application.weather.exception;

import com.nimblesoftwares.ttrilha_api.application.ApplicationException;

public class WeatherServiceException extends ApplicationException {

  public WeatherServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}

