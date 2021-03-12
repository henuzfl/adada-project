package com.dataclouds.event.producer;

import com.dataclouds.event.common.EventMessage;
import com.dataclouds.event.common.jdbc.CommonJdbcOperations;
import com.dataclouds.event.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: zfl
 * @Date: 2020/8/14 16:00
 * @Version: 1.0.0
 */
@Slf4j
public class MysqlSqlMessagePolling implements IMessagePolling {

    @Autowired
    private CommonJdbcOperations commonJdbcOperations;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private final ScheduledExecutorService scheduledExecutorService;

    public MysqlSqlMessagePolling() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(() ->
                        commonJdbcOperations.queryUnPublishedEventMessages()
                                .forEach(this::eventMessageProcess), 0,
                1, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        scheduledExecutorService.shutdownNow();
    }

    private void eventMessageProcess(EventMessage eventMessage) {
        /**
         * 确认已经发送到kafka的topic
         */
        log.info("发送领域事件：" + eventMessage);

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(eventMessage.getEventAggregateType(),
                        eventMessage.getEventAggregateId(),
                        JsonUtils.toJsonStr(eventMessage));
        future.addCallback(new ListenableFutureCallback<SendResult<String,
                String>>() {
            @Override
            public void onFailure(Throwable ex) {
                /**
                 * 发送失败，重试
                 */
                log.info("发送失败：{}", ex);
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                /**
                 * 发送成功
                 */
                commonJdbcOperations.setEventMessagePublished(eventMessage.getId());
            }
        });

    }
}
