package com.aurthor.config;

import cn.hutool.core.io.LineHandler;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: 限流配置
 */
@Configuration
public class RequestRateLimitConfig {

    /**
     * ip限流
     *
     * @return
     */
    @Bean("hostAddrKeyResolver")
    @Primary
    public KeyResolver hostAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    /**
     * 用户id限流
     *
     * @return
     */
    @Bean("userIdKeyResolver")
    public KeyResolver userIdKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }

    /**
     * api接口限流
     *
     * @return
     */
    @Bean("apiKeyResolver")
    public KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());

    }
}
