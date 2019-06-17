package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.core.model.GetTasksResponse;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.service_response.TaskResponse;
import it.sevenbits.spring_homework.web.service.taskservice.TaskService;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

/**
 * Task controller class.
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
@Controller
@RequestMapping("/tasks")
public class TaskListController {

    private final TaskService service;

    /**
     * Controller constructor.
     *
     * @param service application layer to use with controller
     */
    public TaskListController(final TaskService service) {
        this.service = service;
    }

    /**
     * Gets all tasks with the specified status.
     *
     * @param status status to take tasks with
     * @param order order in which to take tasks
     * @param page page to show tasks from
     * @param size current size of each page
     * @return specified page of tasks with specific status in the specific order of the specific size
     */
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<GetTasksResponse> getAllTasks(final @RequestParam(name = "status", defaultValue = "inbox") String status,
                                                        final @RequestParam(name = "order", defaultValue = "desc") String order,
                                                        final @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                        final @RequestParam(name = "size", defaultValue = "25") Integer size) {
        UriComponentsBuilder rootBuilder = UriComponentsBuilder.fromPath("/tasks");
        GetTasksResponse response = service.getTasks(status, order, page, size, rootBuilder);
        return ResponseEntity.ok(response);
    }
    /**
     * Gets the task by the specified id.
     *
     * @param id task to get id by
     * @return the task with the specified id
     */
    @RequestMapping(value = "/{id}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> getTaskById(final @PathVariable("id") String id) {
        if (id.matches(Regexps.UUID)) {
            Task task = service.findTaskById(id);
            if (task == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Creates a new task.
     *
     * @param newTask task creation request
     * @return body with location of the created task
     */
    @RequestMapping(method = POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> create(final @RequestBody AddTaskRequest newTask) {
        if (newTask.getText() == null || newTask.getText().equals("")) {
            return ResponseEntity.badRequest().body(null);
        }
        Task createdTask = service.create(newTask);
        URI location = UriComponentsBuilder.fromPath("/tasks/")
                .path(createdTask.getId())
                .build()
                .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Deletes a task.
     *
     * @param id path variable of the task
     * @return empty body with some code
     */
    @RequestMapping(value = "/{id}", method = DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> deleteTask(final @PathVariable("id") String id) {
        if (id.matches(Regexps.UUID)) {
            Task response = service.removeTaskById(id);
            if (response != null) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Patches a task.
     *
     * @param id - path variable of the task
     * @param request task updating request object
     * @return body with some code
     */
    @RequestMapping(value = "/{id}", method = PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> updateTask(final @PathVariable("id") String id, final @RequestBody UpdateTaskRequest request) {
        if (id.matches(Regexps.UUID)) {
            TaskResponse response = service.editTaskById(request, id);
            if (response.getCode() == null) {
                return ResponseEntity.noContent().build();
            } else {
                return response.getCode().getEntity();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
