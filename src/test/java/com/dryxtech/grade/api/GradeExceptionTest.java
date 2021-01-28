package com.dryxtech.grade.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GradeExceptionTest {

    @Test
    public void testConstructor_Message() {

        String message = "test";

        Exception exception = assertThrows(GradeException.class, () -> { throw new GradeException(message); });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructor_Null() {

        Exception exception = assertThrows(GradeException.class, () -> { throw new GradeException(null); });
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructor_MessageAndCause() {

        String message = "test";
        Throwable cause = new IllegalStateException("foobar");

        Exception exception = assertThrows(GradeException.class, () -> { throw new GradeException(message, cause); });
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructor_MessageAndNull() {

        String message = "test";

        Exception exception = assertThrows(GradeException.class, () -> { throw new GradeException(message, null); });
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
}
