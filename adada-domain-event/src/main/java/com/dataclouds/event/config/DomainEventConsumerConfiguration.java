package com.dataclouds.event.config;

import com.dataclouds.event.consumer.DomainEventMessageConsumer;
import com.dataclouds.event.consumer.DomainEventMessageConsumerImpl;
import com.dataclouds.event.consumer.DomainEventMessageDispatcher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: zfl
 * @Date: 2020/8/18 16:48
 * @Version: 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(DomainEventMessageDispatcher.class)
@Import({CommonJdbcConfiguration.class})
public class DomainEventConsumerConfiguration {

    @Bean
    @ConditionalOnMissingBean(DomainEventMessageConsumer.class)
    public DomainEventMessageConsumer eventMessageConsumer(){
        return new DomainEventMessageConsumerImpl();
    }

    @Bean
    @ConditionalOnMissingBean(DomainEventMessageDispatcher.class)
    public DomainEventMessageDispatcher eventMessageDispatcher(DomainEventMessageConsumer eventMessageConsumer){
        return new DomainEventMessageDispatcher(eventMessageConsumer);
    }
}
