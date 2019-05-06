package it.sevenbits.spring_homework.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetTasksResponse {

    @JsonProperty("_meta")
    private TasksPaginationMeta meta;
    @JsonProperty("tasks")
    private List<Task> tasks;

    public GetTasksResponse(final TasksPaginationMeta meta, @JsonProperty final List<Task> tasks) {
        this.meta = meta;
        this.tasks = tasks;
    }

    public TasksPaginationMeta getMeta() {
        return meta;
    }

    public void setMeta(final TasksPaginationMeta meta) {
        this.meta = meta;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(final List<Task> tasks) {
        this.tasks = tasks;
    }
}
