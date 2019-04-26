package it.sevenbits.spring_homework.config;

import it.sevenbits.spring_homework.core.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
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
     * @param jdbcOperations current jdbc settings
     * @return specific task repository implementation
     */
    @Bean
    @Qualifier("repository")
    public TaskRepository taskRepository(
            @Qualifier("tasksJdbcOperations") final JdbcOperations jdbcOperations,
            @Qualifier("dateGetter") final DateGetter dateGetter) {
        return new DatabaseTaskRepository(jdbcOperations, dateGetter);
    }
}
