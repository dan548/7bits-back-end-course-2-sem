package it.sevenbits.spring_homework.config.constant;

public enum StatusType {

    INBOX("inbox"), DONE("done");

    StatusType(final String type) {
        this.type = type;
    }

    private String type;

    @Override
    public String toString() {
        return type;
    }
}
