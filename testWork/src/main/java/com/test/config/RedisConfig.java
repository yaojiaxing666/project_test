package com.test.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * redis配置类
 * @program: springbootdemo
 * @Date: 2019/1/25 15:20
 * @Author: Mr.Zheng
 * @Description:
 */
//@Configuration
//@EnableCaching //开启注解
public class RedisConfig extends CachingConfigurerSupport {

/**
     * retemplate相关配置
     * @param factory
     * @return
     */

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        // 值采用json序列化
        template.setValueSerializer(jacksonSeial);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);
        template.afterPropertiesSet();

        return template;
    }

/**
     * 自定义缓存管理器，注入RedisCacheManager（不使用springboot默认提供的），
     * 下面缓存管理器配置了缓存失效时间，(如果有其他需求，需要重新定义缓存管理器，在使用缓存注解时指定对应的缓存管理器)
     * 过期时间只对Cache的那几个注解有效比如（@Cacheable，@CachePut），跟redisTemplate对象添加的缓存无关
     * 以及cache注解存取数据的序列化设置
     * @param factory
     * @return
     */

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        //（取出数据序列化配置）
        // 配置在使用 @Cacheable(value="'stus'",key="'stu:1'")注解，当第二次从缓存中读取
        // 数据时，无法将json字符串转换为java Bean的问题，不配置则抛出异常
        // （java.lang.ClassCastException: java.util.LinkedHashMap cannot be cast to
        // com.springboot.enty.Stu）
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //（存入数据序列化配置）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config
                //配置缓存失效时间100秒
                .entryTtl(Duration.ofSeconds(100))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration)
                .build();
        return cacheManager;
    }

/**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     *//*

    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    */
/**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     *//*

    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    */
/**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     *//*

    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    */
/**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     *//*

    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    */
/**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
