package com.nimblesoftwares.ttrilha_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblesoftwares.ttrilha_api.application.trail.port.out.GeocodingPort;
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

    RedisCacheConfiguration geoConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new Jackson2JsonRedisSerializer<>(GeocodingPort.LatLon.class)));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(defaultConfig.entryTtl(Duration.ofHours(4)))
        .withCacheConfiguration(
            "geocoding-results",
            geoConfig.entryTtl(Duration.ofDays(7)))
        .withCacheConfiguration(
            "overpass-trails",
            defaultConfig.entryTtl(Duration.ofHours(4)))
        .build();
  }
}