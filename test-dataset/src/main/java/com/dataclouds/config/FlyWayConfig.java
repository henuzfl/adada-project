package com.dataclouds.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * @Author: zfl
 * @Date: 2020/4/20 16:16
 * @Version: 1.0.0
 */
@Configuration
public class FlyWayConfig {

    @Autowired
    private DataSource dataSource;

    @Value("${spring.flyway.locations}")
    private String[] locations;

    @PostConstruct
    public void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(locations)
                .baselineOnMigrate(true)
                .load();
        flyway.repair();
        flyway.migrate();
    }
}
