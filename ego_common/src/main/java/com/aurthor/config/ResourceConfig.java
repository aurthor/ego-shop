package com.aurthor.config;

import cn.hutool.core.io.FileUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: 统一解析token
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {


    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("aurthor");
        return jwtAccessTokenConverter;
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().disable();  // 基于token，所以不需要session
        http.authorizeRequests().antMatchers(
                "/v2/api-docs",
                "/v3/api-docs",
                "/swagger-resources/configuration/ui",  //用来获取支持的动作
                "/swagger-resources",                   //用来获取api-docs的URI
                "/swagger-resources/configuration/security",//安全选项
                "/webjars/**",
                "/swagger-ui/**",
                "/druid/**",
                "/actuator/**"
        ).permitAll()
                .antMatchers("/**").authenticated()
                .and().headers().cacheControl();
    }
}
