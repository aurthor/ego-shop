package com.aurthor.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: MybatisPlus配置
 */
@Configuration
@MapperScan(basePackages = {"com.aurthor.mapper"})
public class MybatisPlusConfig {

    /**
     * mybatis的分页插件
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
