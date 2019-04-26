package it.sevenbits.spring_homework.core.repository.database;

import it.sevenbits.spring_homework.config.constant.StatusType;
import it.sevenbits.spring_homework.core.dategetter.DateGetter;
import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;
import it.sevenbits.spring_homework.core.model.Task;
import it.sevenbits.spring_homework.core.model.response.TaskResponse;
import it.sevenbits.spring_homework.core.repository.TaskRepository;
import it.sevenbits.spring_homework.web.model.requests.AddTaskRequest;
import it.sevenbits.spring_homework.web.model.requests.UpdateTaskRequest;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * PostgreSQL task repository implementation.
 */
public class DatabaseTaskRepository implements TaskRepository {

    private final JdbcOperations jdbcOperations;
    private final DateGetter dateGetter;

    /**
     * Constructs a new repository.
     * @param jdbcOperations current jdbc settings
     * @param dateGetter date getter
     */
    public DatabaseTaskRepository(final JdbcOperations jdbcOperations, final DateGetter dateGetter) {
        this.jdbcOperations = jdbcOperations;
        this.dateGetter = dateGetter;
    }

    private static Task mapRow(final ResultSet resultSet, final int i) throws SQLException {
        String id = resultSet.getString("id");
        String text = resultSet.getString("text");
        String status = resultSet.getString("status");
        String createdAt = resultSet.getString("createdAt");
        String updatedAt = resultSet.getString("updatedAt");
        return new Task(id, text, status, createdAt, updatedAt);
    }

    @Override
    public TaskResponse findTaskById(final String givenId) {
        List<Task> list = jdbcOperations.query(
                "SELECT id, text, status, createdAt FROM task WHERE id = ?",
                DatabaseTaskRepository::mapRow, givenId);
        if (list.size() >= 1) {
            return new TaskResponse(list.get(0));
        } else {
            return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
        }
    }

    @Override
    public TaskResponse create(final AddTaskRequest request) {
        String date = dateGetter.getDate();
        Task newTask = new Task(UUID.randomUUID().toString(), request.getText(), StatusType.INBOX.toString(), date, date);
        jdbcOperations.update("INSERT INTO task (id, text, status, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)",
                newTask.getId(), newTask.getText(), newTask.getStatus(), newTask.getCreatedAt(), newTask.getUpdatedAt());
        return new TaskResponse(newTask);
    }

    @Override
    public TaskResponse removeTaskById(final String id) {
        TaskResponse response = findTaskById(id);
        if (response.getCode() == null) {
            jdbcOperations.update("DELETE FROM task WHERE id=?", id);
        }
        return response;
    }

    @Override
    public List<Task> getAllTasks() {
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat FROM task",
                (resultSet, i) -> mapRow(resultSet, 0));
    }

    @Override
    public TaskResponse editTaskById(final UpdateTaskRequest request, final String id) {
        if (request.getStatus() != null) {
            if (request.getStatus().equals(StatusType.INBOX.toString()) || request.getStatus().equals(StatusType.DONE.toString())) {
                if (request.getText() != null && !request.getText().equals("")) {
                    String date = dateGetter.getDate();
                    if (jdbcOperations.update("UPDATE task SET status = ?, text = ?, updatedat = ? WHERE id = ?",
                            request.getStatus(), request.getText(), date, id) > 0) {
                        return new TaskResponse(TaskResponseErrorCode.OK);
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                } else {
                    String date = dateGetter.getDate();
                    if (jdbcOperations.update("UPDATE task SET status = ?, updatedat = ? WHERE id = ?",
                            request.getStatus(), date, id) > 0) {
                        return new TaskResponse(TaskResponseErrorCode.OK);
                    }
                    return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
                }
            } else {
                return new TaskResponse(TaskResponseErrorCode.BAD_STATUS);
            }
        } else {
            if (request.getText() == null || request.getText().equals("")) {
                return new TaskResponse(TaskResponseErrorCode.BAD_REQUEST);
            } else {
                String date = dateGetter.getDate();
                if (jdbcOperations.update("UPDATE task SET text = ?, updatedat = ? WHERE id = ?",
                        request.getText(), date, id) > 0) {
                    return new TaskResponse(TaskResponseErrorCode.OK);
                }
                return new TaskResponse(TaskResponseErrorCode.NOT_FOUND);
            }
        }
    }

    @Override
    public List<Task> getTasksWithStatus(final String status) {
        return jdbcOperations.query(
                "SELECT id, text, status, createdat, updatedat FROM task WHERE status=?",
                DatabaseTaskRepository::mapRow, status);
    }
}
