package com.aurthor.config;

import com.aurthor.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: JWT相关配置
 */
@Configuration
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailServiceImpl userDetailService;
    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * 使用jwt存放token
     *
     * @return
     */
    @Bean
    public JwtTokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }


    /**
     * 我们使用非对称加密的方式
     *
     * @return
     */
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("aurthor");
        return jwtAccessTokenConverter;
    }

    /**
     * 描述: 一个web平台，用于第三方访问
     * 一个sxt平台，内部访问的，例如mq里发起远程调用
     *
     * @param clients:
     * @return void
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("web")
                .secret(passwordEncoder.encode("web-secret"))
                .scopes("read")
                .authorizedGrantTypes("password")
                .accessTokenValiditySeconds(7200)
                .and()
                .withClient("sxt")
                .secret(passwordEncoder.encode("sxt-secret"))
                .scopes("write", "read")
                .authorizedGrantTypes("client_credentials")
                .accessTokenValiditySeconds(Integer.MAX_VALUE);
        super.configure(clients);
    }

    /**
     * 端点配置，使用自己的UserService，使用jwt
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .tokenStore(jwtTokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
        super.configure(endpoints);
    }
}
