package it.sevenbits.spring_homework.core.service.dategetter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateGetterTest {

    @Test
    public void testGetSet() {
        DateGetter getter = new DateGetter("yyyy-MM-dd");
        getter.setDatePattern("yyyy-MM-dd HH:mm:ssZ");
        assertEquals("yyyy-MM-dd HH:mm:ssZ", getter.getDatePattern());
    }

}
