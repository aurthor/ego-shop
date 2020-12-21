package com.aurthor.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurthor.constant.GatewayConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description:
 */
@Configuration
public class GatewayConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 描述: 给授权专门做的存入token路由
     *
     * @param builder:
     * @return org.springframework.cloud.gateway.route.RouteLocator
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("auth-server-router", r -> r.path("/oauth/**")
                        .filters(f -> f.modifyResponseBody(String.class, String.class, (exchanges, s) -> {
                            String path = exchanges.getRequest().getURI().getPath();
                            if ("/oauth/token".equals(path)) {
                                //如果是登录的请求，那么得到的s就是token的一套
                                JSONObject tokenObject = JSONUtil.parseObj(s);
                                if (tokenObject.containsKey("access_token")) {
                                    //如果包含access_token就放进redis里面
                                    String access_token = tokenObject.getStr("access_token");
                                    Long expires_in = tokenObject.getLong("expires_in");
                                    redisTemplate
                                            .opsForValue()
                                            .set(GatewayConstant.OAUTH_PREFIX + access_token, "", Duration.ofSeconds(expires_in)
                                            );
                                }
                            }
                            return Mono.just(s);
                        })).uri("lb://auth-server"))
                .build();
    }
}
