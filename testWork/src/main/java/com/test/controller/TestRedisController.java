package com.test.controller;

import com.test.model.PeopleInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@Api(description = "测试redis缓存")
public class TestRedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/saveRedis")
    @ApiImplicitParam(name = "name",value = "redis缓存的key",required = true)
    public PeopleInfo saveRedis(String name){
        PeopleInfo peopleInfo = new PeopleInfo();
        peopleInfo.setAge(18);
        peopleInfo.setSex("男");
        redisTemplate.opsForValue().set(name,peopleInfo,60, TimeUnit.SECONDS);
        PeopleInfo p = (PeopleInfo) redisTemplate.opsForValue().get(name);
        return p;
    }

    @Async
    public void redisAsync(){
        System.out.println(Thread.currentThread().getName()+" :redis");
    }

}
