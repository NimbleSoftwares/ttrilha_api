package com.nimblesoftwares.ttrilha_api.adapter.out.trail;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.OverpassMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.dto.OverpassResponse;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.TrailData;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;
import com.nimblesoftwares.ttrilha_api.domain.trail.model.BoundingBox;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Client to search trails on Overpass API.
 */
public class OverpassClient implements OverpassPort {

  private static final Logger log = LoggerFactory.getLogger(OverpassClient.class);

  private final String overpassBaseUrl;
  private final OverpassMapper overpassMapper;

  private WebClient webClient;

  public OverpassClient(String overpassBaseUrl, OverpassMapper overpassMapper) {
    this.overpassMapper = overpassMapper;
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
  @Cacheable(value = "overpass-trails", key = "#bbox.toTileKey()")
  public List<TrailData> searchTrails(BoundingBox bbox) {
    String query = buildOverpassQuery(bbox);

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
      return List.of();
    }

    Map<Long, TrailData> map = response.elements().stream()
        .map(overpassMapper::toTrailData)
        .filter(Objects::nonNull)
        .collect(Collectors.toMap(
            TrailData::osmId,
            t -> t,
            (t1, t2) ->
                t1.geometry().size() > t2.geometry().size()
                    ? t1 : t2
        ));

    return new ArrayList<>(map.values());
  }

  private String buildOverpassQuery(BoundingBox bbox) {

    String query = "[out:json][timeout:15];" +
        "(" +
        "relation[\"type\"=\"route\"][\"route\"=\"hiking\"]("+ bbox.getSouth() + "," + bbox.getWest() + "," + bbox.getNorth() + "," + bbox.getEast() +");" +
        "relation[\"type\"=\"route\"][\"route\"=\"walking\"]("+ bbox.getSouth() + "," + bbox.getWest() + "," + bbox.getNorth() + "," + bbox.getEast() +");" +
        ");" +
        "out geom;";

    return query;
  }
}