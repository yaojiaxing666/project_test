package com.test.controller;

import com.test.model.UserInfo;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
@Log
public class CacheController {

    /*
    * cacheNames: 支持同一个方法关联多个缓存。这种情况下，当执行方法之前，这些关联的每一个缓存都会被检查，
    * 而且只要至少其中一个缓存命中了，那么这个缓存中的值就会被返回。
    *
    * key: 缓存的时候存的key
    *
    * unless：执行后判断,不缓存的条件。unless 接收一个结果为 true 或 false 的表达式，表达式支持 SpEL。
    * 当结果为 true 时,不缓存。  表达式中的result为返回的对象，对象类型可以用 result.id 获取属性。
    * 如：unless = "#result=='false'"。表示结果为false时不进行缓存。
    *
    * condition： 接收一个结果为 true 或 false 的表达式，表达式同样支持 SpEL 。
    * 如果表达式结果为 true，则调用方法时会执行正常的缓存逻辑，此条件用来判断输入参数的条件。
    * 如：condition = "#id =='123'",表事id等于123时才进行缓存。
    *
    * sync：是否同步，true/false。在一个多线程的环境中，某些操作可能被相同的参数并发地调用，
    * 这样同一个 value 值可能被多次计算（或多次访问 db），这样就达不到缓存的目的。
    * 针对这些可能高并发的操作，我们可以使用 sync 参数来告诉底层的缓存提供者将缓存的入口锁住，这样就只能有一个线程计算操作的结果值，
    * 而其它线程需要等待，这样就避免了 n-1 次数据库访问。sync = true 可以有效的避免缓存击穿的问题。
    *
    * 过期时间在 redisConfig 中配置 .entryTtl(过期时间)
    *
    * */
    @GetMapping("/getCache")
    @Cacheable(cacheNames = {"testCache","cache"},key = "#id",unless = "#result=='false'")
    public String getCache(String id, UserInfo userInfo,boolean flag){
        if (flag){
            return "spring cache 存成功了 success!";
        }else {
            return "false";
        }
    }

    @GetMapping("/testCondition")
    @Cacheable(cacheNames = {"testCache"},key = "#id",condition = "#id=='123'")
    public String testCondition(String id, UserInfo userInfo,boolean flag){
        log.info("userInfo:"+userInfo.toString());
        if (flag){
            return userInfo.toString();
        }else {
            return "false";
        }
    }
}
