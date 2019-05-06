package it.sevenbits.spring_homework.core.service.taskservice;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.GetTasksResponse;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.TasksPaginationMeta;
import it.sevenbits.spring_homework.core.model.service_response.TaskResponse;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

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
     * Gets task page with meta.
     * @param status status of the tasks
     * @param order order of tasks
     * @param page current page
     * @param size page size
     * @param builder uri for pagination
     * @return response
     */
    public GetTasksResponse getTasks(final String status, final String order, final Integer page,
                                     final Integer size, final UriComponentsBuilder builder) {
        if (!"inbox".equals(status) && !"done".equals(status) || !"desc".equals(order) && !"asc".equals(order)
                || page == null || size == null || size < 10 || size > 50 || page < 1) {
            return null;
        }
        Integer fullSize = taskRepository.getSize(status);
        if (fullSize != null) {
            int pages = (int) Math.ceil((double) fullSize / size);
            if (page > pages) {
                return null;
            }
            String next, prev, first, last;
            if (page < pages) {
                next = builder.queryParam("status", status)
                        .queryParam("order", order)
                        .queryParam("page", page + 1)
                        .queryParam("size", size)
                        .toUriString();
                builder.replaceQueryParam("status").replaceQueryParam("order").replaceQueryParam("page").replaceQueryParam("size");
            } else {
                next = null;
            }
            if (page > 1) {
                prev = builder.queryParam("status", status)
                        .queryParam("order", order)
                        .queryParam("page", page - 1)
                        .queryParam("size", size)
                        .toUriString();
                builder.replaceQueryParam("status").replaceQueryParam("order").replaceQueryParam("page").replaceQueryParam("size");
            } else {
                prev = null;
            }
            first = builder.queryParam("status", status)
                    .queryParam("order", order)
                    .queryParam("page", 1)
                    .queryParam("size", size)
                    .toUriString();
            builder.replaceQueryParam("status").replaceQueryParam("order").replaceQueryParam("page").replaceQueryParam("size");
            last = builder.queryParam("status", status)
                    .queryParam("order", order)
                    .queryParam("page", pages)
                    .queryParam("size", size)
                    .toUriString();
            builder.replaceQueryParam("status").replaceQueryParam("order").replaceQueryParam("page").replaceQueryParam("size");
            TasksPaginationMeta meta = new TasksPaginationMeta(fullSize, page, size, next, prev, first, last);
            return new GetTasksResponse(meta, taskRepository.getTaskPage(status, order, page, size));
        } else {
            return null;
        }
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
