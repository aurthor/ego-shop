package com.aurthor.config;

import cn.hutool.core.util.ObjectUtil;
import com.aurthor.constant.GatewayConstant;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: 检测JWT
 */
@Configuration
public class CheckJwtFilter implements Ordered, GlobalFilter {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // 得到请求的路径 如果是登录之类的就放行，如果不是就检查是否有token
        if (GatewayConstant.ALLOW_PATH.contains(path)) {
            return chain.filter(exchange);
        }
        // 从请求头里面拿到jwt
        HttpHeaders headers = exchange.getRequest().getHeaders();
        List<String> list = headers.get(GatewayConstant.AUTHORIZATION);
        if (ObjectUtil.isNotEmpty(list)) {
            //如果list不是空
            String auth = list.get(0);
            String authorization = auth.replaceAll("bearer", "");
            if (!StringUtils.isEmpty(authorization) && redisTemplate.hasKey(GatewayConstant.OAUTH_PREFIX + authorization.trim())) {
                //如果有jwt 就看和redis里面的是不是一样的
                return chain.filter(exchange);
            }
        }
        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().add("content-type", "application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.UNAUTHORIZED.value());
        map.put("msg", "非法访问");
        ObjectMapper objectMapper = new ObjectMapper();
        byte[] bytes = null;
        DataBuffer buffer = null;
        try {
            bytes = objectMapper.writeValueAsBytes(map);
            buffer = response.bufferFactory().wrap(bytes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
