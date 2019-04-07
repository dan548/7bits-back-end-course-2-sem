package it.sevenbits.spring_homework.core.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

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
    public TaskRepository taskRepository() {
        return new SimpleTaskRepository(new HashMap<>());
    }
}
