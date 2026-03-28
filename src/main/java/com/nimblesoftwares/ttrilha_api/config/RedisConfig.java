package com.nimblesoftwares.ttrilha_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.application.trail.dto.ExploreTrailResult;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.CurrentWeatherResult;
import com.nimblesoftwares.ttrilha_api.application.weather.dto.ForecastResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

  @Bean
  public RedisCacheManager redisCacheManager(
      RedisConnectionFactory connectionFactory,
      ObjectMapper objectMapper) {

    RedisCacheConfiguration defaultConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer(objectMapper)));

    RedisCacheConfiguration overpassConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new Jackson2JsonRedisSerializer<>(ExploreTrailResult.class)));

    RedisCacheConfiguration geoConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new Jackson2JsonRedisSerializer<>(GeocodingPort.LatLon.class)));

    RedisCacheConfiguration weatherCurrentConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new Jackson2JsonRedisSerializer<>(CurrentWeatherResult.class)));

    RedisCacheConfiguration weatherForecastConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new Jackson2JsonRedisSerializer<>(ForecastResult.class)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig.entryTtl(Duration.ofMinutes(5)))
        .withCacheConfiguration(
            "geocoding-results",
            geoConfig.entryTtl(Duration.ofDays(7)))
        .withCacheConfiguration(
            "overpass-trails",
            overpassConfig.entryTtl(Duration.ofHours(4)))
        .withCacheConfiguration(
            "weather-current",
            weatherCurrentConfig.entryTtl(Duration.ofMinutes(10)))
        .withCacheConfiguration(
            "weather-forecast",
            weatherForecastConfig.entryTtl(Duration.ofMinutes(10)))
        .build();
  }
}