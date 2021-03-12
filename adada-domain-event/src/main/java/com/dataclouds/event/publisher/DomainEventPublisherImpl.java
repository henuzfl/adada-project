package com.dataclouds.event.publisher;

import com.dataclouds.event.common.BaseDomainEvent;
import com.dataclouds.event.common.EventMessage;
import com.dataclouds.event.producer.IMessageProducer;
import com.dataclouds.event.utils.JsonUtils;

import java.util.List;

/**
 * @Author: zfl
 * @Date: 2020/7/13 16:39
 * @Version: 1.0.0
 */
public class DomainEventPublisherImpl implements IDomainEventPublisher {

    private IMessageProducer messageProducer;

    public DomainEventPublisherImpl(IMessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @Override
    public void publish(List<BaseDomainEvent> events) {
        events.stream().forEach(event -> messageProducer.send(makeEventMessageForDomainEvent(event)));
    }

    private static EventMessage makeEventMessageForDomainEvent(BaseDomainEvent event) {
        EventMessage eventMessage = new EventMessage();
        eventMessage.setMsgId(event.getId());
        eventMessage.setEventAggregateId(event.getAggregateId());
        eventMessage.setEventAggregateType(event.getAggregateType());
        eventMessage.setEventClassName(event.getEventClassName());
        eventMessage.setBody(JsonUtils.toJsonStr(event));
        return eventMessage;
    }
}
