package com.dataclouds.event.producer;

import com.dataclouds.event.common.EventMessage;

/**
 * @Author: zfl
 * @Date: 2020/7/13 16:45
 * @Version: 1.0.0
 */
public interface IMessageProducer {

    void send(EventMessage eventMessage);
}
