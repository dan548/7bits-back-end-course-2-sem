package it.sevenbits.spring_homework.web.service;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.GetTasksResponse;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.TasksPaginationMeta;
import it.sevenbits.spring_homework.core.model.service_response.TaskResponse;
import it.sevenbits.spring_homework.core.repository.tasks.TaskRepository;
import it.sevenbits.spring_homework.web.model.taskrequests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.taskrequests.UpdateTaskRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Task manipulating service.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final int minSize;
    private final int maxSize;

    /**
     * Creates a task service.
     * @param taskRepository repository to be used by service
     */
    public TaskService(final TaskRepository taskRepository, @Value("${parameters.min-page-size}") final int minSize,
                       @Value("${parameters.max-page-size}") final int maxSize) {
        this.taskRepository = taskRepository;
        this.minSize = minSize;
        this.maxSize = maxSize;
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
                || page == null || size == null || size < minSize || size > maxSize || page < 1) {
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
    public Task findTaskById(final String id) {
        return taskRepository.findTaskById(id);
    }

    /**
     * Creates a task from the request.
     * @param request request to build the task
     * @return response
     */
    public Task create(final AddTaskRequest request) {
        return taskRepository.create(request);
    }

    /**
     * Removes a task with the specified id.
     * @param id id of the task to delete
     * @return response with the task
     */
    public Task removeTaskById(final String id) {
        if (id.matches(Regexps.UUID)) {
            return taskRepository.removeTaskById(id);
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
                    int changed = taskRepository.editTaskById(request, id);
                    if (changed != 0) {
                        return new TaskResponse();
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                } else {
                    return new TaskResponse(TaskResponseErrorCode.BAD_STATUS);
                }
            } else {
                if (request.getText() == null || request.getText().equals("")) {
                    return new TaskResponse(TaskResponseErrorCode.BAD_REQUEST);
                } else {
                    int changed = taskRepository.editTaskById(request, id);
                    if (changed != 0) {
                        return new TaskResponse();
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                }
            }
        }
        return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
    }
}
