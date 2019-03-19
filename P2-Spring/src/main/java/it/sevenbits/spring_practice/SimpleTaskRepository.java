package it.sevenbits.spring_practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SimpleTaskRepository implements TaskRepository {

    private List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public Task create(AddTaskRequest request) {
        Task task = new Task(UUID.randomUUID().toString(), request.getText());
        tasks.add(task);
        return task;
    }
}
