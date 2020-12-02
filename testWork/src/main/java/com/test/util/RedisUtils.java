package com.test.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean lock(String key,String value){
        Boolean b = redisTemplate.opsForValue().setIfAbsent(key,value);
        if(b){
            redisTemplate.expire(key,3, TimeUnit.SECONDS);
        }
        return b;
    }

    public boolean unlock(String key,String value){
        String achieve = (String) redisTemplate.opsForValue().get(key);
        Boolean b = false;
        if(value.equals(achieve)){
            b = redisTemplate.delete(key);
        }
        return b;
    }

}
