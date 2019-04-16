package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * PostgreSQL task repository implementation.
 */
public class DatabaseTaskRepository implements TaskRepository {

    private final JdbcOperations jdbcOperations;

    /**
     * Constructs a new repository.
     * @param jdbcOperations current jdbc settings
     */
    public DatabaseTaskRepository(final JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public Task findTaskById(final String id) throws DatabaseException {
        throw new DatabaseException("The method is not implemented yet.");
    }

    @Override
    public Task create(final AddTaskRequest request) {
        Task newTask = new Task(UUID.randomUUID().toString(), request.getText());
        jdbcOperations.update("INSERT INTO task (id, text, status, createdAt) VALUES (?, ?, ?, ?)",
                newTask.getId(), newTask.getText(), newTask.getStatus(), newTask.getCreatedAt());
        return newTask;
    }

    @Override
    public void removeTaskById(final String id) throws DatabaseException {
        throw new DatabaseException("The method is not implemented yet.");
    }

    @Override
    public List<Task> getAllTasks() {
        return jdbcOperations.query(
                "SELECT * FROM task",
                (resultSet, i) -> {
                    String id = resultSet.getString(1);
                    String text = resultSet.getString(2);
                    String status = resultSet.getString(3);
                    String createdAt = resultSet.getString(4);
                    return new Task(id, text, status, createdAt);
                });
    }

    @Override
    public List<Task> getTasksFiltered(final Predicate<Task> predicate) {
        return getAllTasks().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
