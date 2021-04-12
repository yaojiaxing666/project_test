package com.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * maxInactiveIntervalInSeconds: 设置 Session 失效时间
 * 使用 Redis Session 之后，原 Spring Boot 中的 server.session.timeout 属性不再生效。
 * 用以session和redis得联合存储，可以实现多台负载session通过redis共享
 *
 * @author jinjiayu
 * @since 2020/12/18
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=1800)
public class SessionConfig {
}
