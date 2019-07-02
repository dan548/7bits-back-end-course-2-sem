package it.sevenbits.spring_homework.config.constant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

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

    public static HashSet<String> getEnums() {
        return Arrays.stream(values())
                .map(x -> x.type)
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public String toString() {
        return type;
    }
}
