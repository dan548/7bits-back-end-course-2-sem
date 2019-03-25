package it.sevenbits.spring_homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Bean
    public TaskRepository taskRepository() {
        return new SimpleTaskRepository();
    }
}
