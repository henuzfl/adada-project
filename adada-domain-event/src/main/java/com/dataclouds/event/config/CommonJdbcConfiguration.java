package com.dataclouds.event.config;

import com.dataclouds.event.common.jdbc.CommonJdbcOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zfl
 * @Date: 2020/8/19 18:06
 * @Version: 1.0.0
 */
@Configuration
public class CommonJdbcConfiguration {

    @Bean
    @ConditionalOnMissingBean(CommonJdbcOperations.class)
    public CommonJdbcOperations commonJdbcOperations() {
        return new CommonJdbcOperations();
    }
}
