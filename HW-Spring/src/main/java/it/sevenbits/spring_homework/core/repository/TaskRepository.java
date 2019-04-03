package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.web.models.Task;
import it.sevenbits.spring_homework.web.models.requests.AddTaskRequest;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface TaskRepository {

    Task findTaskById(String id);
    Task create(AddTaskRequest request);
    void removeTaskById(String id);
    Map<String, Task> getTasks();
    List<Task> getTasks(Predicate<Task> predicate);

}
