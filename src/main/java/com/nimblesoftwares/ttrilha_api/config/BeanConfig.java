package com.nimblesoftwares.ttrilha_api.config;

import com.nimblesoftwares.ttrilha_api.adapter.out.persistence.trail.mapper.LineStringMapper;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.GeocodingClient;
import com.nimblesoftwares.ttrilha_api.adapter.out.trail.OverpassClient;
import com.nimblesoftwares.ttrilha_api.application.trail.port.in.SaveTrailUseCase;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.OverpassPort;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.TrailRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.trail.service.ExploreTrailsService;
import com.nimblesoftwares.ttrilha_api.application.trail.service.SaveTrailFromOverpassService;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserIdentityRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.port.out.UserRepositoryPort;
import com.nimblesoftwares.ttrilha_api.application.user.service.SaveUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

  @Bean
  public GeocodingPort geocodingPort() {
    return new GeocodingClient();
  }

  @Bean
  public OverpassPort overpassPort(@Value("${overpass.baseUrl}") String baseUrl) {
    return new OverpassClient(baseUrl);
  }

  @Bean
  public LineStringMapper lineStringMapper() {
    return new LineStringMapper();
  }

  @Bean
  public ExploreTrailsService exploreTrailsService(
      GeocodingPort geocodingPort,
      OverpassPort overpassPort) {
    return new ExploreTrailsService(overpassPort, geocodingPort);
  }

  @Bean
  public SaveTrailUseCase saveTrailUseCase(
      TrailRepositoryPort trailRepositoryPort,
      LineStringMapper lineStringMapper) {
    return new SaveTrailFromOverpassService(lineStringMapper, trailRepositoryPort);
  }

  @Bean
  public SaveUserService saveUserService(
      UserRepositoryPort userRepositoryPort,
      UserIdentityRepositoryPort userIdentityRepository
  ) {
    return new SaveUserService(userIdentityRepository, userRepositoryPort);
  }
}
