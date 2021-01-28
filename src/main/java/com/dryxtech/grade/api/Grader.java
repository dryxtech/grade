package com.dryxtech.grade.api;

/**
 * This interface represents a grader.
 *
 * @param <T> Input Type
 * @author Drew Griffin
 * @since 1.0
 */
@FunctionalInterface
public interface Grader<T> {

    /**
     * Method to grade input and return a grade value.
     *
     * @param t input that grader will grade
     * @return grade value
     * @throws GradeException if grading fails
     */
    GradeValue grade(T t) throws GradeException;
}
