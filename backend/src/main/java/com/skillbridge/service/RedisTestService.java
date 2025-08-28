package com.skillbridge.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    private final StringRedisTemplate redisTemplate;

    public RedisTestService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String testRedis() {
        try {
            // Write a test key
            redisTemplate.opsForValue().set("redis_test_key", "Hello Redis!");

            // Read it back
            String value = redisTemplate.opsForValue().get("redis_test_key");

            return "Redis connection successful! Value: " + value;
        } catch (Exception e) {
            return "Redis connection failed: " + e.getMessage();
        }
    }
}
