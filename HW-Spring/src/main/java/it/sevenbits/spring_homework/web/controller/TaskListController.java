package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.config.constant.Regexps;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.response.TaskResponse;
import it.sevenbits.spring_homework.core.service.taskservice.TaskService;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;


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
     * @return all tasks with specific status
     */
    @RequestMapping(method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Task> getAllTasks(final @RequestParam(name = "status", defaultValue = "inbox") String status) {

        return service.getTasksWithStatus(status);
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
            TaskResponse task = service.findTaskById(id);
            if (task == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(task.getTask());
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
        TaskResponse createdTask = service.create(newTask);
        URI location = UriComponentsBuilder.fromPath("/tasks/")
                .path(createdTask.getTask().getId())
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
            TaskResponse response = service.removeTaskById(id);
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
