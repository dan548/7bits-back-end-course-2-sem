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

/**
 * Task manipulating service.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Creates a task service.
     * @param taskRepository repository to be used by service
     */
    public TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Gets tasks with the specified status.
     * @param status status of the tasks
     * @return list of tasks
     */
    public List<Task> getTasksWithStatus(final String status) {
        return taskRepository.getTasksWithStatus(status);
    }

    /**
     * Gets task by id.
     * @param id id of the task
     * @return the specified task response
     */
    public TaskResponse findTaskById(final String id) {
        Task task = taskRepository.findTaskById(id);

        if (task == null) {
            return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
        } else {
            return new TaskResponse(task);
        }
    }

    /**
     * Creates a task from the request.
     * @param request request to build the task
     * @return response
     */
    public TaskResponse create(final AddTaskRequest request) {
        return new TaskResponse(taskRepository.create(request));
    }

    /**
     * Removes a task with the specified id.
     * @param id id of the task to delete
     * @return response with the task
     */
    public TaskResponse removeTaskById(final String id) {
        if (id.matches(Regexps.UUID)) {
            Task task = taskRepository.removeTaskById(id);
            if (task != null) {
                return new TaskResponse(task);
            }
        }
        return null;
    }

    /**
     * Edits task with the specified id.
     * @param request request to edit
     * @param id id of the task to edit
     * @return edited task response
     */
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
                        return new TaskResponse(task);
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                }
            }
        }
        return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
    }
}
