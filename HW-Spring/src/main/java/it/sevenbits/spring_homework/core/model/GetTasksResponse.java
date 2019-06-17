package it.sevenbits.spring_homework.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Contains the list to show with meta.
 */
public class GetTasksResponse {

    @JsonProperty("_meta")
    private TasksPaginationMeta meta;
    @JsonProperty("tasks")
    private List<Task> tasks;

    /**
     * List of tasks with meta.
     * @param meta information about the current presentation
     * @param tasks list of tasks to show
     */
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GetTasksResponse response = (GetTasksResponse) o;
        return Objects.equals(meta, response.meta) &&
                Objects.equals(tasks, response.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, tasks);
    }
}
