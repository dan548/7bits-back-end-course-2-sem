package it.sevenbits.spring_homework.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    private String owner;

    /**
     * Constructs a task.
     * @param id id field of the task
     * @param text text field of the task
     * @param status status field of the task
     * @param createdAt date when the task was created
     * @param updatedAt date when the task was last updated
     * @param owner owner id
     */
    public Task(final String id, final String text, final String status,
                final String createdAt, final String updatedAt, final String owner) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
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

    void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }
}
