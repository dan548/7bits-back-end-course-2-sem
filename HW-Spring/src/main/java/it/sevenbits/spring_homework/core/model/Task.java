package it.sevenbits.spring_homework.core.model;

/**
 * Task model.
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
public class Task {

    private final String id;
    private String text;
    private String status;
    private String createdAt;
    private String updatedAt;

    /**
     * Constructs a task.
     * @param id id field of the task
     * @param text text field of the task
     * @param status status field of the task
     * @param createdAt date when the task was created
     * @param updatedAt date when the task was last updated
     */
    public Task(final String id, final String text, final String status, final String createdAt, final String updatedAt) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
