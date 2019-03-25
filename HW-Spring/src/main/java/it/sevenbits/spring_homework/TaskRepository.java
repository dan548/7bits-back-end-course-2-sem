package it.sevenbits.spring_homework;

import java.util.List;

public interface TaskRepository {

    List<Task> getAllTasks();
    Task create(AddTaskRequest request);

}
