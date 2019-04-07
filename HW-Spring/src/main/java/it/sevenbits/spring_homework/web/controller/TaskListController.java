package it.sevenbits.spring_homework.web.controller;

import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import it.sevenbits.spring_homework.core.repository.TaskRepository;

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

    private final TaskRepository repository;

    /**
     * Controller constructor.
     *
     * @param repository repository implementation to use with controller
     */
    public TaskListController(final TaskRepository repository) {
        this.repository = repository;
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

        return repository.getTasksFiltered(task -> task.getStatus().equals(status));
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
        Task task = repository.findTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
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
        Task createdTask = repository.create(newTask);
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
        Task task = repository.findTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        repository.removeTaskById(id);
        return ResponseEntity.ok().build();
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
        Task task = repository.findTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        if (request.getStatus() != null) {
            if (((request.getStatus().equals("done")) || (request.getStatus().equals("inbox")))) {
                task.setStatus(request.getStatus());
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            if (request.getText() == null || request.getText().equals("")) {
                return ResponseEntity.badRequest().build();
            } else {
                task.setText(request.getText());
                return ResponseEntity.noContent().build();
            }
        }

        if (request.getText() != null) {
            if (!request.getText().equals("")) {
                task.setText(request.getText());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        return ResponseEntity.noContent().build();
    }
}
