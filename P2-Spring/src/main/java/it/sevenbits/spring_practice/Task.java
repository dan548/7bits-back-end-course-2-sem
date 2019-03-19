package it.sevenbits.spring_practice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Task {

    private final String id;
    private final String text;

    @JsonCreator
    public Task(@JsonProperty("id") String id, @JsonProperty("text") String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
