package com.nimblesoftwares.ttrilha_api.adapter.in.web.weather;

import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetCurrentWeatherUseCase;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetForecastUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

  private final GetCurrentWeatherUseCase getCurrentWeatherUseCase;
  private final GetForecastUseCase getForecastUseCase;

  public WeatherController(GetCurrentWeatherUseCase getCurrentWeatherUseCase,
                           GetForecastUseCase getForecastUseCase) {
    this.getCurrentWeatherUseCase = getCurrentWeatherUseCase;
    this.getForecastUseCase = getForecastUseCase;
  }

  /**
   * GET /api/v1/weather/current?city={city}
   * Returns the current weather for a given city name.
   */
  @GetMapping("/current")
  public ResponseEntity<CurrentWeatherResult> getCurrentWeather(
      @RequestParam String city) {

    CurrentWeatherResult result = getCurrentWeatherUseCase.execute(city);
    return ResponseEntity.ok(result);
  }

  /**
   * GET /api/v1/weather/forecast?lat={lat}&lon={lon}&date={YYYY-MM-DD}
   * Returns the weather forecast (3-hour slots) for a given coordinate and date (up to 5 days ahead).
   */
  @GetMapping("/forecast")
  public ResponseEntity<ForecastResult> getForecast(
      @RequestParam double lat,
      @RequestParam double lon,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

    ForecastResult result = getForecastUseCase.execute(lat, lon, date);
    return ResponseEntity.ok(result);
  }
}

