package it.sevenbits.spring_homework.core.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class RepositoryConfig {
    @Bean
    public TaskRepository taskRepository() {
        return new SimpleTaskRepository(new HashMap<>());
    }
}
