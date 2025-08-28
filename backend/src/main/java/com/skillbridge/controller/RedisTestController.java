package com.skillbridge.controller;

import com.skillbridge.service.RedisTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    private final RedisTestService redisTestService;

    public RedisTestController(RedisTestService redisTestService) {
        this.redisTestService = redisTestService;
    }

    @GetMapping("/redis-test")
    public String testRedis() {
        return redisTestService.testRedis();
    }
}
