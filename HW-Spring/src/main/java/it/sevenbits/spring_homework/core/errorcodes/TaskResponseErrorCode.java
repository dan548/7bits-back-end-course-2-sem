package it.sevenbits.spring_homework.core.errorcodes;

import it.sevenbits.spring_homework.core.model.Task;
import org.springframework.http.ResponseEntity;

/**
 * Error codes for task responses.
 */
public enum TaskResponseErrorCode {
    /**
     * Returned when the status is invalid.
     */
    BAD_STATUS("Invalid status.", ResponseEntity.badRequest().build()),
    /**
     * Returned if the request is invalid.
     */
    BAD_REQUEST("Invalid request.", ResponseEntity.badRequest().build()),
    /**
     * Code is returned if there is no element with such id.
     */
    NOT_FOUND("Element with such id is not found.", ResponseEntity.notFound().build()),
    /**
     * OK code.
     */
    OK("okay", null);

    private String errorCode;
    private ResponseEntity<Task> entity;

    /**
     * Constructs error code with specified description and response entity.
     * @param errorCode error description
     * @param entity response entity
     */
    TaskResponseErrorCode(final String errorCode, final ResponseEntity<Task> entity) {
        this.errorCode = errorCode;
        this.entity = entity;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public ResponseEntity<Task> getEntity() {
        return entity;
    }
}
