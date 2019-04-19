package it.sevenbits.spring_homework.core.dategetter;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateGetter {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx");

    public static String getDate() {
        return ZonedDateTime.now(ZoneOffset.UTC).format(DATE_FORMATTER);
    }

}
