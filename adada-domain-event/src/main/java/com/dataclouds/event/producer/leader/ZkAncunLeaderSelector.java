package com.dataclouds.event.producer.leader;

import lombok.extern.log4j.Log4j2;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.CancelLeadershipException;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: zfl
 * @Date: 2020/8/25 9:57
 * @Version: 1.0.0
 */
@Log4j2
public class ZkAncunLeaderSelector extends LeaderSelectorListenerAdapter implements AncunLeaderSelector, LeaderSelectorListener {

    private CuratorFramework client;
    private String path;
    private String name;

    private final LeaderSelector leaderSelector;
    private final Runnable leaderSelectedCallback;
    private final Runnable leaderRemovedCallback;

    private final AtomicInteger leaderCount = new AtomicInteger();


    public ZkAncunLeaderSelector(CuratorFramework client, String path,
                                 String name, Runnable leaderSelectedCallback
            , Runnable leaderRemovedCallback) {
        this.client = client;
        this.path = path;
        this.name = name;
        this.leaderSelectedCallback = leaderSelectedCallback;
        this.leaderRemovedCallback = leaderRemovedCallback;
        leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
    }

    @Override
    public void start() {
        log.info("starting leader selector");
        leaderSelector.start();
    }

    @Override
    public void stop() {
        log.info("Closing leader selector, name : {}", this.name);
        leaderSelector.close();
        log.info("Closed leader selector, name : {}", this.name);
    }

    @Override
    public void takeLeadership(CuratorFramework client) {
        CountDownLatch stopCountDownLatch = new CountDownLatch(1);
        try {
            log.info("Calling leaderSelectedCallback, leaderId : {}", name);
            this.leaderSelectedCallback.run();
            log.info("Called leaderSelectedCallback, leaderId : {}", name);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("Calling leaderRemovedCallback, leaderId : {}", name);
            leaderRemovedCallback.run();
            log.info("Called leaderRemovedCallback, leaderId : {}", name);
            return;
        }
        try {
            stopCountDownLatch.await();
        } catch (InterruptedException e) {
            log.error("Leadership interrupted", e);
        }
        log.info("Calling leaderRemovedCallback, leaderId : {}", name);
        leaderRemovedCallback.run();
        log.info("Called leaderRemovedCallback, leaderId : {}", name);
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework,
                             ConnectionState connectionState) {
        log.info("StateChanged, state : {}, leaderId : {}", connectionState,
                this.name);
        if (connectionState == ConnectionState.SUSPENDED || connectionState == ConnectionState.LOST) {
            throw new CancelLeadershipException();
        }
    }
}
