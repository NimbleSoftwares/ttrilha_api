package com.nimblesoftwares.ttrilha_api.adapter.out.trail;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Convert location name to coordinates using Nominatim API.
 */
public class GeocodingClient implements GeocodingPort {

  private static final Logger log = LoggerFactory.getLogger(GeocodingClient.class);

  private WebClient webClient;

  public GeocodingClient() {
    init();
  }

  @PostConstruct
  void init() {
    this.webClient = WebClient.builder()
        .baseUrl("https://nominatim.openstreetmap.org")
        .defaultHeader("User-Agent", "ttrilha-api/1.0")
        .build();
  }

  @Cacheable(value = "geocoding-results", key = "T(org.apache.commons.lang3.StringUtils).stripAccents(#locationName.toLowerCase())")
  @Override
  public LatLon geocode(String locationName) {
    try {
      String encoded = URLEncoder.encode(locationName, StandardCharsets.UTF_8);

      JsonNode response = webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/search")
              .queryParam("q", locationName)
              .queryParam("polygon_geojson", "1")
              .queryParam("format", "jsonv2")
              .queryParam("limit", 1)
              .build()
          )
          .retrieve()
          .bodyToMono(JsonNode.class)
          .block();

      if (response == null || !response.isArray() || response.isEmpty()) {
        return null;
      }

      JsonNode result = response.get(0);
      double lat = result.get("lat").asDouble();
      double lon = result.get("lon").asDouble();

      return new LatLon(lat, lon);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to geocode location: " + locationName, e);
    }
  }

}
// Exemplo de retorno do nominatim
 /**
 *     {
 *         "place_id": 6119205,
 *         "licence": "Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright",
 *         "osm_type": "node",
 *         "osm_id": 875023148,
 *         "lat": "-25.2527438",
 *         "lon": "-48.8087788",
 *         "category": "natural",
 *         "type": "peak",
 *         "place_rank": 18,
 *         "importance": 0.2916191994999862,
 *         "addresstype": "peak",
 *         "name": "Pico Paraná",
 *         "display_name": "Pico Paraná, Antonina, Região Geográfica Imediata de Paranaguá, Região Geográfica Intermediária de Curitiba, Paraná, Região Sul, Brasil",
 *         "boundingbox": [
 *             "-25.2527938",
 *             "-25.2526938",
 *             "-48.8088288",
 *             "-48.8087288"
 *         ],
 *         "geojson": {
 *             "type": "Point",
 *             "coordinates": [
 *                 -48.8087788,
 *                 -25.2527438
 *             ]
 *         }
 *     }
 * ]
 */