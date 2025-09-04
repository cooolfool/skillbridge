package com.skillbridge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class FailoverDataSourceConfig {

    @Value("${spring.datasource.primary.url}")
    private String primaryUrl;

    @Value("${spring.datasource.primary.username}")
    private String primaryUser;

    @Value("${spring.datasource.primary.password}")
    private String primaryPassword;

    @Value("${spring.datasource.secondary.url}")
    private String secondaryUrl;

    @Value("${spring.datasource.secondary.username}")
    private String secondaryUser;

    @Value("${spring.datasource.secondary.password}")
    private String secondaryPassword;

    @Bean
    public DataSource dataSource() {
        try {
            DataSource primary = DataSourceBuilder.create()
                    .url(primaryUrl)
                    .username(primaryUser)
                    .password(primaryPassword)
                    .build();

            try (Connection conn = primary.getConnection()) {
                // If Neon is reachable, use it
                return primary;
            }
        } catch (Exception e) {
            // Fallback to Render
            return DataSourceBuilder.create()
                    .url(secondaryUrl)
                    .username(secondaryUser)
                    .password(secondaryPassword)
                    .build();
        }
    }
}

