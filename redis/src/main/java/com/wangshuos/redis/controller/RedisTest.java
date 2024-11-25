package com.wangshuos.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisTest
 * @Author wangshuo
 * @Date 2024/8/19 17:08
 * @Version 1.0
 **/
@RestController
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @RequestMapping("/setValue")
    public String setValue(@RequestParam String key, @RequestParam String value, @RequestParam long timeout){
        //redisTemplate.opsForValue().set(key, value, Duration.ofMillis(timeout));
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        //redisTemplate.opsForValue().multiSet(map);

        redisTemplate.opsForHash().putAll("map",map);
        redisTemplate.expire("map", timeout, TimeUnit.SECONDS);

        return "Value set successfully!";
    }

    @RequestMapping("/setTimeout")
    public Boolean setTimeout(@RequestParam long timeout){

        return redisTemplate.expire("map", timeout, TimeUnit.SECONDS);
    }

    @RequestMapping("/getValue")
    public Object getValue(@RequestParam String key, @RequestParam Object hashKey){
        return redisTemplate.opsForHash().get(key,hashKey);
    }



}
