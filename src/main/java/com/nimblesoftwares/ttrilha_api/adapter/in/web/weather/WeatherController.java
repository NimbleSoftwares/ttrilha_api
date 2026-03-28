package com.nimblesoftwares.ttrilha_api.adapter.in.web.weather;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.application.user.port.in.GetUserIdBySubUseCase;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetCurrentWeatherUseCase;
import com.nimblesoftwares.ttrilha_api.application.weather.port.in.GetForecastUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/weather")
public class WeatherController {

  private final GetCurrentWeatherUseCase getCurrentWeatherUseCase;
  private final GetForecastUseCase getForecastUseCase;
  private final GetUserIdBySubUseCase getUserIdBySubUseCase;

  public WeatherController(GetCurrentWeatherUseCase getCurrentWeatherUseCase,
                           GetForecastUseCase getForecastUseCase,
                           GetUserIdBySubUseCase getUserIdBySubUseCase) {
    this.getCurrentWeatherUseCase = getCurrentWeatherUseCase;
    this.getForecastUseCase = getForecastUseCase;
    this.getUserIdBySubUseCase = getUserIdBySubUseCase;
  }

  /**
   * GET /api/v1/weather/current
   * Resolves the logged-in user's city from their profile and returns current weather.
   */
  @GetMapping("/current")
  public ResponseEntity<CurrentWeatherResult> getCurrentWeather(
      @AuthenticationPrincipal Jwt jwt) {

    UUID userId = resolveCurrentUserId(jwt);
    return ResponseEntity.ok(getCurrentWeatherUseCase.execute(userId));
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

    return ResponseEntity.ok(getForecastUseCase.execute(lat, lon, date));
  }

  private UUID resolveCurrentUserId(Jwt jwt) {
    ProviderIdentity pi = ProviderIdentity.fromSub(jwt.getSubject());
    return getUserIdBySubUseCase.execute(pi.provider(), pi.providerUserId());
  }
}
