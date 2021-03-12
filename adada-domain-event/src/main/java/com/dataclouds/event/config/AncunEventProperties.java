package com.dataclouds.event.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: zfl
 * @Date: 2020/8/28 16:06
 * @Version: 1.0.0
 */
@ConfigurationProperties(prefix = "ancun.event")
public class AncunEventProperties {

    private final Zookeeper zookeeper = new Zookeeper();

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public static class Zookeeper {
        private String connectString;

        public String getConnectString() {
            return connectString;
        }

        public void setConnectString(String connectString) {
            this.connectString = connectString;
        }
    }


}
