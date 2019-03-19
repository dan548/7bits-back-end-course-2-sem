package it.sevenbits.spring_practice;

import java.util.List;

public interface TaskRepository {

    List<Task> getAllTasks();
    Task create(AddTaskRequest request);

}
