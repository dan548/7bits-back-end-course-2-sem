package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Simple task repository implementation.
 */
public class SimpleTaskRepository implements TaskRepository {

    private Map<String, Task> tasks;

    /**
     * Constructs a simple task repository.
     *
     * @param tasks specific map implementation
     */
    SimpleTaskRepository(final Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public Task findTaskById(final String id) {
        return tasks.get(id);
    }

    @Override
    public void removeTaskById(final String id) {
        tasks.remove(id);
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task create(final AddTaskRequest request) {
        String id = UUID.randomUUID().toString();
        Task task = new Task(id, request.getText());
        tasks.put(id, task);
        return task;
    }

    @Override
    public List<Task> getTasksFiltered(final Predicate<Task> predicate) {
        return tasks.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
