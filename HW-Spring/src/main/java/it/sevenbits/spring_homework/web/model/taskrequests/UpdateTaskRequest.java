package it.sevenbits.spring_homework.web.model.taskrequests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Task updating request model.
 *
 * @since 1.0
 * @version 1.0
 * @author Daniil Polyakov
 */
public class UpdateTaskRequest {

    private final String status;
    private final String text;

    /**
     * Constructs a task updating request.
     *
     * @param status status field of the request
     * @param text text field of the request
     */
    @JsonCreator
    public UpdateTaskRequest(final @JsonProperty("status") String status, final @JsonProperty("text") String text) {
        this.status = status;
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }
}
