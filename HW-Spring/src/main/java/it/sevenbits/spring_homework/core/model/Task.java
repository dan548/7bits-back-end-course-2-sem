package it.sevenbits.spring_homework.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
}
