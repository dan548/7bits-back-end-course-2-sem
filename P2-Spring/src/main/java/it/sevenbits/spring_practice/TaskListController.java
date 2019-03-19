package it.sevenbits.spring_practice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/tasks")
public class TaskListController {

    private final TaskRepository repository;

    public TaskListController(TaskRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method=GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Task> getAllTasks() {
        return repository.getAllTasks();
    }

    @RequestMapping(method=POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Task> create(@RequestBody AddTaskRequest newTask) {
        if (newTask.getText() == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Task createdTask = repository.create(newTask);
        URI location = UriComponentsBuilder.fromPath("/tasks/")
                .path(createdTask.getId())
                .build()
                .toUri();
        return ResponseEntity.created(location).body(createdTask);
    }
}
