package it.sevenbits.spring_homework.core.model.service_response;

import it.sevenbits.spring_homework.core.errorcodes.TaskResponseErrorCode;

/**
 * Model of the task response.
 */
public class TaskResponse {

    private final TaskResponseErrorCode code;

    /**
     * Constructs response with a task.
     */
    public TaskResponse() {
        code = null;
    }

    /**
     * Constructs response with error code.
     * @param code the code to wrap
     */
    public TaskResponse(final TaskResponseErrorCode code) {
        this.code = code;
    }

    public TaskResponseErrorCode getCode() {
        return code;
    }
}
