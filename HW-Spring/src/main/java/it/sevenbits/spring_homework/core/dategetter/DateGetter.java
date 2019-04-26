package it.sevenbits.spring_homework.core.dategetter;


import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateGetter {

    private String datePattern;

    public DateGetter(final String pattern) {
        this.datePattern = pattern;
    }

    public String getDate() {
        return ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(datePattern));
    }

}
