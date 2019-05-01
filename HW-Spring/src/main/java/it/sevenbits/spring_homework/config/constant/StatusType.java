package it.sevenbits.spring_homework.config.constant;

/**
 * Enum of the status types.
 */
public enum StatusType {

    /**
     * Status types.
     */
    INBOX("inbox"), DONE("done");

    /**
     * Constructs a type.
     * @param type string representation
     */
    StatusType(final String type) {
        this.type = type;
    }

    private String type;

    @Override
    public String toString() {
        return type;
    }
}
