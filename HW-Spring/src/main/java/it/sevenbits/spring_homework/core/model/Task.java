package it.sevenbits.spring_homework.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");

    private static String getDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DATE_FORMATTER);
    }

    /**
     * Constructs a task.
     *
     * @param id id field of the task
     * @param text text field of the task
     */
    @JsonCreator
    public Task(final @JsonProperty("id") String id, final @JsonProperty("text") String text) {
        this.id = id;
        this.text = text;
        status = "inbox";
        createdAt = getDate();
        updatedAt = createdAt;
    }

    /**
     * Reconstructs a task from the database.
     * @param id id field of the task
     * @param text text field of the task
     * @param status status field of the task
     * @param createdAt date when the task was created
     */
    public Task(final String id, final String text, final String status, final String createdAt) {
        this.id = id;
        this.text = text;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Reconstructs a task from the database.
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
