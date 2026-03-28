package com.nimblesoftwares.ttrilha_api.adapter.in.web.exception;

import com.nimblesoftwares.ttrilha_api.application.weather.exception.WeatherCityNotFoundException;
import com.nimblesoftwares.ttrilha_api.application.weather.exception.WeatherServiceException;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class WeatherExceptionHandler {

  private final Tracer tracer;

  public WeatherExceptionHandler(Tracer tracer) {
    this.tracer = tracer;
  }

  @ExceptionHandler(WeatherCityNotFoundException.class)
  public ProblemDetail handleWeatherCityNotFoundException(WeatherCityNotFoundException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.warn(e.getMessage());
    return ExceptionHandlerHelper.createProblemDetail(
        HttpStatus.NOT_FOUND, "City not found", e.getMessage(), traceId);
  }

  @ExceptionHandler(WeatherServiceException.class)
  public ProblemDetail handleWeatherServiceException(WeatherServiceException e) {
    String traceId = ExceptionHandlerHelper.getTraceId(tracer);
    log.error(e.getMessage(), e);
    return ExceptionHandlerHelper.createProblemDetail(
        HttpStatus.BAD_GATEWAY, "Weather service error", "Failed to retrieve weather data. Please try again later.", traceId);
  }
}

