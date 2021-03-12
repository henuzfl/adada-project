package com.dataclouds.event.producer;

import com.dataclouds.event.common.EventMessage;
import com.dataclouds.event.common.jdbc.CommonJdbcOperations;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zfl
 * @Date: 2020/7/17 17:54
 * @Version: 1.0.0
 */
public class MessageProducerByJdbcImpl implements IMessageProducer {

    @Autowired
    private CommonJdbcOperations commonJdbcOperations;

    @Override
    public void send(EventMessage eventMessage) {
        commonJdbcOperations.insertEventMessage(eventMessage);
    }
}
