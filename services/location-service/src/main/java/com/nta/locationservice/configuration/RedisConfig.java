package com.nta.locationservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
  @Value("${spring.data.redis.host}")
  String REDIS_HOST;

  @Value("${spring.data.redis.port}")
  String REDIS_PORT;

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration redisConfig =
        new RedisStandaloneConfiguration(REDIS_HOST, Integer.parseInt(REDIS_PORT));

    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(100); // Số lượng tối đa connection trong pool
    poolConfig.setMaxIdle(50); // Connection tối đa có thể idle
    poolConfig.setMinIdle(20); // Connection tối thiểu idle

    JedisClientConfiguration jedisClientConfiguration =
        JedisClientConfiguration.builder().usePooling().poolConfig(poolConfig).build();

    return new JedisConnectionFactory(redisConfig, jedisClientConfiguration);
  }

  @Bean
  @Scope("prototype")
  GeoOperations<String, String> geoOperations(RedisTemplate<String, String> template) {
    return template.opsForGeo();
  }

  @Bean
  RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());

    JdkSerializationRedisSerializer jdkSerializationRedisSerializer =
        new JdkSerializationRedisSerializer();
    template.setDefaultSerializer(jdkSerializationRedisSerializer);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(jdkSerializationRedisSerializer);
    template.setHashKeySerializer(new StringRedisSerializer());

    template.afterPropertiesSet();
    return template;
  }
}
