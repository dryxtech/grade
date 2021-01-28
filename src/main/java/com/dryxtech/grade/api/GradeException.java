package com.dryxtech.grade.api;

/**
 * The parent class for all grade related exceptions.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeException extends Exception {

    public GradeException(final String message) {
        super(message);
    }

    public GradeException(final String message, final Throwable t) {
        super(message, t);
    }
}
