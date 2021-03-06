package com.dataclouds.event.consumer;

import com.dataclouds.event.common.EventMessage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author: zfl
 * @Date: 2020/8/19 16:19
 * @Version: 1.0.0
 */
@Getter
public class DomainEventHandlers {
    private List<DomainEventHandler> handlers;

    public DomainEventHandlers(List<DomainEventHandler> handlers) {
        this.handlers = handlers;
    }

    public static DomainEventHandlersBuilder builder() {
        return new DomainEventHandlersBuilder();
    }

    public static class DomainEventHandlersBuilder {
        private String aggregateType;
        private List<DomainEventHandler> handlers = new ArrayList<>();

        public DomainEventHandlersBuilder forAggregateType(String aggregateType) {
            this.aggregateType = aggregateType;
            return this;
        }

        public DomainEventHandlersBuilder onDomainEvent(String domainEventClass, Consumer<EventMessage> consumer) {
            this.handlers.add(new DomainEventHandler(this.aggregateType,
                    domainEventClass,
                    e -> consumer.accept(e)));
            return this;
        }

        public DomainEventHandlers build() {
            return new DomainEventHandlers(this.handlers);
        }
    }

    public Set<String> getAllAggregateTypes() {
        return this.handlers.stream()
                .map(DomainEventHandler::getAggregateType)
                .collect(Collectors.toSet());
    }
}
