package com.dataclouds.event.publisher;

import com.dataclouds.event.common.BaseDomainEvent;

import java.util.List;

/**
 * @Author: zfl
 * @Date: 2020/7/13 15:59
 * @Version: 1.0.0
 */
public interface IDomainEventPublisher {
    void publish(List<BaseDomainEvent> events);
}
