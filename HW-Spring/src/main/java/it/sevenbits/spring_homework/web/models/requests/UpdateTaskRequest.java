package it.sevenbits.spring_homework.web.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateTaskRequest {

    private final String status;
    private final String text;

    @JsonCreator
    public UpdateTaskRequest(@JsonProperty("status") String status, @JsonProperty("text") String text) {
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
