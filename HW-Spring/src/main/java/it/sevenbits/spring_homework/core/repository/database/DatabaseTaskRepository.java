package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.core.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
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
    public Task findTaskById(final String givenId) throws DatabaseException {
        if (!givenId.matches(
                "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
        )) {
            throw new DatabaseException("Bad id");
        }
        List<Task> list = jdbcOperations.query(
                "SELECT id, text, status, createdAt FROM task WHERE id = ?",
                (resultSet, i) -> {
                    String id = resultSet.getString(1);
                    String text = resultSet.getString(2);
                    String status = resultSet.getString(3);
                    String createdAt = resultSet.getString(4);
                    return new Task(id, text, status, createdAt);
                }, givenId);
        if (list.size() >= 1) {
            return list.get(0);
        } else {
            throw new DatabaseException("Element with such id is not found.");
        }
    }

    @Override
    public Task create(final AddTaskRequest request) {
        Task newTask = new Task(UUID.randomUUID().toString(), request.getText());
        jdbcOperations.update("INSERT INTO task (id, text, status, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)",
                newTask.getId(), newTask.getText(), newTask.getStatus(), newTask.getCreatedAt(), newTask.getUpdatedAt());
        return newTask;
    }

    @Override
    public void removeTaskById(final String id) throws DatabaseException {
        if (jdbcOperations.update("DELETE FROM task WHERE id=?", id) != 1) {
            throw new DatabaseException("deleting went wrong");
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat FROM task",
                (resultSet, i) -> {
                    String id = resultSet.getString(1);
                    String text = resultSet.getString(2);
                    String status = resultSet.getString(3);
                    String createdAt = resultSet.getString(4);
                    String updatedAt = resultSet.getString(5);
                    return new Task(id, text, status, createdAt, updatedAt);
                });
    }

    @Override
    public Task editTaskById(final UpdateTaskRequest request, final String id) throws DatabaseException {
        Task task = findTaskById(id);

        if (request.getStatus() != null) {
            if (request.getStatus().equals("inbox") || request.getStatus().equals("done")) {
                jdbcOperations.update("UPDATE task SET status=? WHERE id=?", request.getStatus(), id);
                task.setStatus(request.getStatus());
                String date = DateGetter.getDate();
                jdbcOperations.update("UPDATE task SET updatedat=? WHERE id=?", date, id);
                task.setUpdatedAt(date);
            } else {
                throw new DatabaseException("Bad status");
            }
        } else {
            if (request.getText() == null || request.getText().equals("")) {
                throw new DatabaseException("Bad request");
            }
        }
        jdbcOperations.update("UPDATE task SET text=? WHERE id=?", request.getText(), id);
        task.setText(request.getText());
        String date = DateGetter.getDate();
        jdbcOperations.update("UPDATE task SET updatedat=? WHERE id=?", date, id);
        task.setUpdatedAt(date);
        return task;
    }

    @Override
    public List<Task> getTasksFiltered(final Predicate<Task> predicate) {
        return getAllTasks().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
