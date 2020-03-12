package com.opuscapita.peppol.smp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SmpDataSource {

    @Value("${db-init.host:mysql}")
    private String host;

    @Value("${db-init.port:3306}")
    private String port;

    @Value("${db-init.user:root}")
    private String user;

    @Value("${db-init.password:test}")
    private String password;

    @Value("${db-init.database:peppol-smp}")
    private String database;

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .username(user)
                .password(password)
                .url("jdbc:mysql://" + host + ":" + port + "/" + database)
                .driverClassName("com.mysql.jdbc.Driver")
                .build();
    }

}
