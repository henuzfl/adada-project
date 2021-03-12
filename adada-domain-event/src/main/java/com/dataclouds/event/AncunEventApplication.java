package com.dataclouds.event;

import com.dataclouds.event.config.DomainEventConsumerConfiguration;
import com.dataclouds.event.config.DomainEventPublisherConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: zfl
 * @Date: 2020/8/20 9:12
 * @Version: 1.0.0
 */
@Import({DomainEventConsumerConfiguration.class,
        DomainEventPublisherConfiguration.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface AncunEventApplication {
}
