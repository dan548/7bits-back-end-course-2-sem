package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.core.Task;
import it.sevenbits.spring_homework.web.models.requests.AddTaskRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleTaskRepository implements TaskRepository {

    private Map<String, Task> tasks;

    public SimpleTaskRepository(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Task findTaskById(String id) {
        return tasks.get(id);
    }

    @Override
    public void removeTaskById(String id) {
        tasks.remove(id);
    }

    @Override
    public Map<String, Task> getTasks() {
        return tasks;
    }

    @Override
    public Task create(AddTaskRequest request) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, request.getText());
        tasks.put(id, task);
        return task;
    }

    @Override
    public List<Task> getTasks(Predicate<Task> predicate) {
        return tasks.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
