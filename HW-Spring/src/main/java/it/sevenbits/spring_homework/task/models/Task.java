package it.sevenbits.spring_homework.task.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private final String id;
    private final String text;
    private String status;

    @JsonCreator
    public Task(@JsonProperty("id") String id, @JsonProperty("text") String text) {
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

    public void setStatus(String status) {
        this.status = status;
    }
}
