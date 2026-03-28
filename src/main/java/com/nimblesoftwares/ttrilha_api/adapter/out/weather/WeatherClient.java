package com.nimblesoftwares.ttrilha_api.adapter.out.weather;

import com.nimblesoftwares.ttrilha_api.adapter.out.weather.dto.OpenWeatherCurrentResponse;
import com.nimblesoftwares.ttrilha_api.adapter.out.weather.dto.OpenWeatherForecastResponse;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.WeatherCondition;
import com.nimblesoftwares.ttrilha_api.application.weather.exception.WeatherCityNotFoundException;
import com.nimblesoftwares.ttrilha_api.application.weather.exception.WeatherServiceException;
import com.nimblesoftwares.ttrilha_api.application.weather.port.out.WeatherPort;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.util.List;

/**
 * Client to fetch weather data from OpenWeatherMap API.
 */
public class WeatherClient implements WeatherPort {

  private static final Logger log = LoggerFactory.getLogger(WeatherClient.class);
  private static final String BASE_URL = "https://api.openweathermap.org";

  private final String apiKey;
  private WebClient webClient;

  public WeatherClient(String apiKey) {
    this.apiKey = apiKey;
    init();
  }

  @PostConstruct
  void init() {
    this.webClient = WebClient.builder()
        .baseUrl(BASE_URL)
        .build();
  }

  @Override
  @Cacheable(value = "weather-current", key = "T(org.apache.commons.lang3.StringUtils).stripAccents(#city.toLowerCase())")
  public CurrentWeatherResult getCurrentWeatherByCity(String city) {
    log.info("Fetching current weather for city: {}", city);
    try {
      OpenWeatherCurrentResponse response = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/data/2.5/weather")
              .queryParam("q", city)
              .queryParam("units", "metric")
              .queryParam("lang", "pt_br")
              .queryParam("appid", apiKey)
              .build())
          .retrieve()
          .bodyToMono(OpenWeatherCurrentResponse.class)
          .block();

      if (response == null) {
        throw new WeatherCityNotFoundException(city);
      }

      String description = (response.weather() != null && !response.weather().isEmpty())
          ? response.weather().getFirst().description()
          : "";
      int conditionId = (response.weather() != null && !response.weather().isEmpty())
          ? response.weather().getFirst().id()
          : 0;
      WeatherCondition condition = WeatherCondition.fromConditionId(conditionId);
      double windSpeed = response.wind() != null ? response.wind().speed() : 0.0;

      return new CurrentWeatherResult(
          response.name(),
          response.main().temp(),
          response.main().feelsLike(),
          response.main().humidity(),
          description,
          conditionId,
          condition,
          windSpeed
      );
    } catch (WebClientResponseException e) {
      if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
        throw new WeatherCityNotFoundException(city);
      }
      throw new WeatherServiceException("Failed to fetch weather for city: " + city, e);
    } catch (WeatherCityNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new WeatherServiceException("Failed to fetch weather for city: " + city, e);
    }
  }

  @Override
  @Cacheable(value = "weather-forecast", key = "T(java.lang.String).format('%.4f_%.4f_%s', #lat, #lon, #date)")
  public ForecastResult getForecastByCoordinates(double lat, double lon, LocalDate date) {
    log.info("Fetching forecast for lat={}, lon={}, date={}", lat, lon, date);
    try {
      OpenWeatherForecastResponse response = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/data/2.5/forecast")
              .queryParam("lat", lat)
              .queryParam("lon", lon)
              .queryParam("units", "metric")
              .queryParam("lang", "pt_br")
              .queryParam("appid", apiKey)
              .build())
          .retrieve()
          .bodyToMono(OpenWeatherForecastResponse.class)
          .block();

      if (response == null || response.list() == null) {
        throw new WeatherServiceException("Empty forecast response for lat=" + lat + ", lon=" + lon, null);
      }

      String targetDatePrefix = date.toString(); // yyyy-MM-dd

      List<ForecastResult.ForecastEntry> entries = response.list().stream()
          .filter(item -> item.dtTxt() != null && item.dtTxt().startsWith(targetDatePrefix))
          .map(item -> {
            String time = item.dtTxt().length() >= 16 ? item.dtTxt().substring(11, 16) : item.dtTxt();
            String description = (item.weather() != null && !item.weather().isEmpty())
                ? item.weather().getFirst().description()
                : "";
            int conditionId = (item.weather() != null && !item.weather().isEmpty())
                ? item.weather().getFirst().id()
                : 0;
            WeatherCondition condition = WeatherCondition.fromConditionId(conditionId);
            double windSpeed = item.wind() != null ? item.wind().speed() : 0.0;
            return new ForecastResult.ForecastEntry(
                time,
                item.main().temp(),
                item.main().feelsLike(),
                item.main().humidity(),
                description,
                conditionId,
                condition,
                windSpeed
            );
          })
          .toList();

      String cityName = response.city() != null ? response.city().name() : "";

      return new ForecastResult(cityName, date.toString(), entries);
    } catch (WeatherServiceException e) {
      throw e;
    } catch (Exception e) {
      throw new WeatherServiceException("Failed to fetch forecast for lat=" + lat + ", lon=" + lon, e);
    }
  }
}

