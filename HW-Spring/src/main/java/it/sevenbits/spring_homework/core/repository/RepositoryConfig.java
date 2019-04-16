package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.core.repository.database.DatabaseTaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;


/**
 * Configuration class of the repository.
 *
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
@Configuration
public class RepositoryConfig {
    /**
     * Constructs a repository implementation
     * @return specific task repository implementation
     */
    @Bean
    public TaskRepository taskRepository(
            @Qualifier("tasksJdbcOperations") JdbcOperations jdbcOperations) {
        return new DatabaseTaskRepository(jdbcOperations);
    }
}
