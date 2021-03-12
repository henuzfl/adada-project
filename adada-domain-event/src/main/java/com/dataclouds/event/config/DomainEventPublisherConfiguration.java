package com.dataclouds.event.config;

import com.dataclouds.event.annotation.DomainEventAspect;
import com.dataclouds.event.producer.IMessagePolling;
import com.dataclouds.event.producer.IMessageProducer;
import com.dataclouds.event.producer.MessageProducerByJdbcImpl;
import com.dataclouds.event.producer.MysqlSqlMessagePolling;
import com.dataclouds.event.producer.leader.MessagePollingLeadership;
import com.dataclouds.event.publisher.DomainEventPublisherImpl;
import com.dataclouds.event.publisher.IDomainEventPublisher;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: zfl
 * @Date: 2020/7/13 16:56
 * @Version: 1.0.0
 */
@Configuration(proxyBeanMethods = false)
@Import({CommonJdbcConfiguration.class})
@ConditionalOnClass(IDomainEventPublisher.class)
@EnableConfigurationProperties(AncunEventProperties.class)
@EnableAutoConfiguration
public class DomainEventPublisherConfiguration {

    @Bean
    @ConditionalOnMissingBean(IMessageProducer.class)
    public IMessageProducer messageProducer() {
        return new MessageProducerByJdbcImpl();
    }

    @Bean
    @ConditionalOnMissingBean(IMessagePolling.class)
    public IMessagePolling messagePolling() {
        return new MysqlSqlMessagePolling();
    }

    @Bean
    @ConditionalOnMissingBean(MessagePollingLeadership.class)
    public MessagePollingLeadership messagePollingLeadership(IMessagePolling messagePolling, AncunEventProperties ancunEventProperties) {
        return new MessagePollingLeadership(ancunEventProperties.getZookeeper().getConnectString(), messagePolling);
    }

    @Bean
    @ConditionalOnMissingBean(IDomainEventPublisher.class)
    public IDomainEventPublisher domainEventPublisher(IMessageProducer messageProducer) {
        return new DomainEventPublisherImpl(messageProducer);
    }

    @Bean
    @ConditionalOnMissingBean(DomainEventAspect.class)
    public DomainEventAspect domainEventAspect() {
        return new DomainEventAspect();
    }
}
