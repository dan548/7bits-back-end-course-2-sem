package it.sevenbits.spring_homework.core.service.dategetter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date getting service.
 */
public class DateGetter {

    private String datePattern;

    /**
     * Constructs a getter from a pattern.
     * @param pattern pattern of the date
     */
    public DateGetter(final String pattern) {
        this.datePattern = pattern;
    }

    public String getDate() {
        return ZonedDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern(datePattern));
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(final String datePattern) {
        this.datePattern = datePattern;
    }
}
