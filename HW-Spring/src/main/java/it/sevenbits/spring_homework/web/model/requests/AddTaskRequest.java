package it.sevenbits.spring_homework.web.model.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Task adding request model.
 *
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
public class AddTaskRequest {

    private final String text;

    /**
     * Constructs a request/
     *
     * @param text text field of the request
     */
    @JsonCreator
    public AddTaskRequest(final @JsonProperty("text") String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
