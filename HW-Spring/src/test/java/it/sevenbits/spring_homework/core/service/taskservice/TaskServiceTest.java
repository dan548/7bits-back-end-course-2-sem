package it.sevenbits.spring_homework.core.service.taskservice;

import it.sevenbits.spring_homework.core.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class TaskServiceTest {

    private TaskRepository mockRepository;
    private TaskService service;

    @BeforeEach
    public void setup() {
        mockRepository = mock(TaskRepository.class);
        service = new TaskService(mockRepository);
    }

}
