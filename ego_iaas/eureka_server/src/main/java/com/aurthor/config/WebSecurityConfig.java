package com.aurthor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: Security配置
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * http请求的配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //关闭跨站请求攻击
        http.csrf().disable();
        //健康监测的请求放行
        http.authorizeRequests().antMatchers("/actuator/**","/master/**").permitAll();
        super.configure(http);
    }
}
