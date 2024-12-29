package com.nta.websocket.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final ListOperations<String, Object> listOperations;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.listOperations = redisTemplate.opsForList();
    }

    public boolean isRedisLive() {
        try {
            String response = Objects.requireNonNull(redisTemplate.getConnectionFactory())
                    .getConnection()
                    .ping();
            return "PONG".equals(response);
        } catch (Exception e) {
            return false;
        }
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getKey(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public boolean hasValue(String key, String value) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key)) && value.equals(this.getKey(key));
    }

    public Map<String, Object> getField(String key) {
        return hashOperations.entries(key);
    }

    public void setTimeToLive(String key, long timeoutInMinutes) {
        redisTemplate.expire(key, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public void hashSet(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    public boolean hashExists(String key, String field) {
        return hashOperations.hasKey(key, field);
    }

    public Object hashGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    public Map<String, Object> hashGetAll(String key) {
        return hashOperations.entries(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void delete(String key, String field) {
        hashOperations.delete(key, field);
    }

    public boolean checkKeyFieldValueExists(String key, String field, Object value) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            Object currentValue = hashOperations.get(key, field);
            return value.equals(currentValue);
        }
        return false;
    }

    public void addToList(String key, Object value) {
        listOperations.rightPush(key, value);
    }

    public List<Object> getAllFromList(String key) {
        return listOperations.range(key, 0, -1);
    }

    public void deleteList(String key) {
        redisTemplate.delete(key);
    }

    public boolean containsElement(String key, Object value) {
        List<Object> list = listOperations.range(key, 0, -1);
        return list != null && list.contains(value);
    }

    public Long getListSize(String key) {
        return listOperations.size(key);
    }

    public void removeFromList(String key, Object value) {
        listOperations.remove(key, 1, value);
    }
}
