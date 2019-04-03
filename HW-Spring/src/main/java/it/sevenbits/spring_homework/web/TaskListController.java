package it.sevenbits.spring_homework.web;

import it.sevenbits.spring_homework.core.Task;
import it.sevenbits.spring_homework.web.models.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.models.requests.UpdateTaskRequest;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/tasks")
public class TaskListController {

    private final TaskRepository repository;

    public TaskListController(TaskRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method=GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Task> getAllTasks(@RequestParam(name = "status", defaultValue = "inbox") String status) {

        return repository.getTasks().values()
                .stream()
                .filter(task -> task.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    @RequestMapping(value="/{id}", method=GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> getTaskById(@PathVariable("id") String id) {
        Task task = repository.findTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @RequestMapping(method=POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> create(@RequestBody AddTaskRequest newTask) {
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

    @RequestMapping(value="/{id}", method=DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> deleteTask(@PathVariable("id") String id) {
        Task task = repository.findTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        repository.removeTaskById(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value="/{id}", method=PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Task> updateTask(@PathVariable("id") String id, @RequestBody UpdateTaskRequest request) {
        Task task = repository.findTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        if (!request.getStatus().equals("done") && !request.getStatus().equals("inbox")) {
            if (request.getText() != null && !request.getText().equals("")) {
                task.setText(request.getText());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        }
        task.setStatus(request.getStatus());
        if (request.getText() != null && !request.getText().equals("")) {
            task.setText(request.getText());
        }
        return ResponseEntity.noContent().build();
    }
}
