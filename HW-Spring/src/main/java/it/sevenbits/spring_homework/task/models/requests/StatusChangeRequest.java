package it.sevenbits.spring_homework.task.models.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusChangeRequest {

    private final String status;

    @JsonCreator
    public StatusChangeRequest(@JsonProperty("status") String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
