package it.sevenbits.spring_homework.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Task database configuration class.
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
@Configuration
public class TasksDatabaseConfig {

    /**
     * Builds DataSource.
     * @return dataSource
     */
    @Bean
    @FlywayDataSource
    @Qualifier("tasksDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tasks")
    public DataSource tasksDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Builds a JdbcTemplate.
     * @param tasksDataSource used to build JdbcTemplate
     * @return JdbcTemplate
     */
    @Bean
    @Qualifier("tasksJdbcOperations")
    public JdbcOperations tasksJdbcOperations(
            @Qualifier("tasksDataSource")
                    final DataSource tasksDataSource
    ) {
        return new JdbcTemplate(tasksDataSource);
    }
}
