package com.dataclouds.event.producer.leader;

import com.dataclouds.event.producer.IMessagePolling;
import lombok.extern.log4j.Log4j2;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @Author: zfl
 * @Date: 2020/8/25 11:13
 * @Version: 1.0.0
 */
@Log4j2
public class MessagePollingLeadership {

    private static final String ZK_PATH = "/ancun/event/";
    private final String leaderId;
    private volatile boolean leader;
    private IMessagePolling messagePolling;
    private final CuratorFramework client;

    @Value("${spring.application.name}")
    private String applicationName;


    public MessagePollingLeadership(String zkConnectString,
                                    IMessagePolling messagePolling) {
        this.leaderId = UUID.randomUUID().toString();
        this.messagePolling = messagePolling;
        this.client =
                CuratorFrameworkFactory.newClient(zkConnectString,
                        new ExponentialBackoffRetry(1000,
                                Integer.MAX_VALUE));
    }

    @PostConstruct
    public void init() {
        log.info("starting message polling leadership");
        ZkAncunLeaderSelector zkAncunLeaderSelector =
                new ZkAncunLeaderSelector(client, ZK_PATH + applicationName,
                        this.leaderId,
                        this::leaderSelectedCallback,
                        this::leaderRemovedCallback);
        this.client.start();
        zkAncunLeaderSelector.start();
    }

    private void leaderSelectedCallback() {
        log.info("Assigning leadership");
        leader = true;
        messagePolling.start();
        log.info("Assigned leadership");
    }

    private void leaderRemovedCallback() {
        log.info("Resigning leadership");
        leader = false;
        messagePolling.stop();
        log.info("Resigned leadership");
    }
}
