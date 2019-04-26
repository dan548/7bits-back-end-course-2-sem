package it.sevenbits.spring_homework.core.repository;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.response.TaskResponse;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    public TaskResponse findTaskById(final String id) {
        Task task = tasks.get(id);
        if (task == null) {
            return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
        } else {
            return new TaskResponse(task);
        }
    }

    @Override
    public TaskResponse removeTaskById(final String id) {
        Task task = tasks.remove(id);
        if (task != null) {
            return new TaskResponse(task);
        } else {
            return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public TaskResponse create(final AddTaskRequest request) {
        String id = UUID.randomUUID().toString();
        DateGetter getter = new DateGetter("yyyy-MM-dd'T'HH:mm:ssxxx");
        String date = getter.getDate();
        Task task = new Task(id, request.getText(), StatusType.INBOX.toString(), date, date);
        tasks.put(id, task);
        return new TaskResponse(task);
    }

    @Override
    public TaskResponse editTaskById(final UpdateTaskRequest request, final String id) {
        TaskResponse resp = findTaskById(id);
        if (resp.getCode() == null) {
            if (request.getStatus() != null) {
                resp.getTask().setStatus(request.getStatus());
            }
            if (request.getText() != null) {
                resp.getTask().setText(request.getText());
            }
        } else {
            return resp;
        }
        return resp;
    }

    @Override
    public List<Task> getTasksWithStatus(final String status) {
        return getAllTasks().stream()
                .filter(x -> x.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
