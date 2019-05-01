package it.sevenbits.spring_homework.core.service.taskservice;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.response.TaskResponse;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksWithStatus(final String status) {
        return taskRepository.getTasksWithStatus(status);
    }

    public TaskResponse findTaskById(final String id) {
        Task task = taskRepository.findTaskById(id);

        if (task == null) {
            return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
        } else {
            return new TaskResponse(task);
        }
    }

    public TaskResponse create(final AddTaskRequest request) {
        return new TaskResponse(taskRepository.create(request));
    }

    public TaskResponse removeTaskById(final String id) {
        if (id.matches(Regexps.UUID)) {
            Task task = taskRepository.removeTaskById(id);
            if (task != null) {
                return new TaskResponse(task);
            }
        }
        return null;
    }

    public TaskResponse editTaskById(final UpdateTaskRequest request, final String id) {
        if (id.matches(Regexps.UUID)) {
            if (request.getStatus() != null) {
                if (request.getStatus().equals(StatusType.INBOX.toString()) || request.getStatus().equals(StatusType.DONE.toString())) {
                    Task task = taskRepository.editTaskById(request, id);
                    if (task != null) {
                        return new TaskResponse(task);
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                } else {
                    return new TaskResponse(TaskResponseErrorCode.BAD_STATUS);
                }
            } else {
                if (request.getText() == null || request.getText().equals("")) {
                    return new TaskResponse(TaskResponseErrorCode.BAD_REQUEST);
                } else {
                    Task task = taskRepository.editTaskById(request, id);
                    if (task != null) {
                        return new TaskResponse(TaskResponseErrorCode.OK);
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                }
            }
        }
        return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
    }
}
