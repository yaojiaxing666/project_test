package com.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * maxInactiveIntervalInSeconds: 设置 Session 失效时间
 * 使用 Redis Session 之后，原 Spring Boot 中的 server.session.timeout 属性不再生效。
 * redisNamespace 插入到 redis 的 session 命名空间，默认是 spring:session,
 * 完整key为：redisNamespace + :sessions: + session.getId()
 * cleanupCron 过期 session 清理任务，默认是 1 分钟清理一次
 * redisFlushMode 刷新方式 ，其实在上面原理的 flushImmediateIfNecessary 方法中有用到，默认是 ON_SAVE
 * 用以session和redis得联合存储，可以实现多台负载session通过redis共享
 *
 * @author jinjiayu
 * @since 2020/12/18
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=1800,redisNamespace = "project-test")
public class SessionConfig {
}
