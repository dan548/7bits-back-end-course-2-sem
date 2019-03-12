package it.sevenbits.tasks;

import java.util.concurrent.ConcurrentHashMap;

public class TaskRepository {

    private static TaskRepository instance;
    private ConcurrentHashMap<String, Task> tasks;

    private TaskRepository() {
        tasks = new ConcurrentHashMap<>();
    }

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    public void addTask(Task task) {
        tasks.put(task.getId().toString(), task);
    }

    public Task getTask(String id) {
        return tasks.get(id);
    }

    public void deleteTask(String id) {
        tasks.remove(id);
    }

    public ConcurrentHashMap<String, Task> getTasks() {
        return tasks;
    }
}
