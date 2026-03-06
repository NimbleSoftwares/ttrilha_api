package com.nimblesoftwares.ttrilha_api.adapter.out.trail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassResponse;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.ExploreTrailResult;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.OverpassTrailData;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.GeoPoint;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Client to search trails on Overpass API.
 */
public class OverpassClient implements OverpassPort {

  private static final Logger log = LoggerFactory.getLogger(OverpassClient.class);

  private final String overpassBaseUrl;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private WebClient webClient;

  public OverpassClient(String overpassBaseUrl) {
    this.overpassBaseUrl = overpassBaseUrl;
    init();
  }

  @PostConstruct
  void init() {
    this.webClient = WebClient.builder()
        .baseUrl(overpassBaseUrl)
        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
        .build();
  }

  @Override
  @Cacheable(value = "overpass-trails", key = "T(java.lang.String).format('%.5f_%.5f_%d', #lat, #lon, #radiusKm)")
  public ExploreTrailResult searchTrails(double lat, double lon, int radiusKm) {

    String query = "[out:json][timeout:25];" +
        "(" +
        "relation[\"type\"=\"route\"][\"route\"=\"hiking\"](around:" + (radiusKm * 1000) + "," + lat + "," + lon + ");" +
        "relation[\"type\"=\"route\"][\"route\"=\"walking\"](around:" + (radiusKm * 1000) + "," + lat + "," + lon + ");" +
        ");" +
        "out geom;";

    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("data", query);

    OverpassResponse response = webClient.post()
        .uri("/interpreter")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .bodyValue(body)
        .retrieve()
        .bodyToMono(OverpassResponse.class)
        .block();

    if (response == null || response.elements() == null) {
      return new ExploreTrailResult(List.of());
    }

    List<OverpassTrailData> trailsData = response.elements().stream()
       .map(element -> {
         String name = element.tags() != null ? element.tags().getOrDefault("name", "Unnamed") : "Unnamed";
         List<GeoPoint> points = (
             element.geometry() != null && !element.geometry().isEmpty())
             ? element.geometry()
             : (element.members() != null)
                 ? element.members().stream()
                     .filter(m -> m.geometry() != null)
                     .flatMap(m -> m.geometry().stream())
                      .toList()
                 : List.of();

          return new OverpassTrailData(element.id(), name, element.tags(), points);
       }).toList();

    return new ExploreTrailResult(trailsData);
  }
}