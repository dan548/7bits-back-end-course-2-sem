package it.sevenbits.servlets;

import java.util.UUID;

public class Task {

    private String name;
    private UUID id;

    public Task(String name, UUID id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
