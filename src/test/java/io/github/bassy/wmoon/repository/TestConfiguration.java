package io.github.bassy.wmoon.repository;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;

@Configuration
public class TestConfiguration {
    @Bean
    public Destination destination(DataSource dataSource) {
        return new DataSourceDestination(dataSource);
    }
}
