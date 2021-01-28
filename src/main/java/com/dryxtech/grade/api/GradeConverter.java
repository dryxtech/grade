package com.dryxtech.grade.api;

/**
 * This interface represents a grade converter.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface GradeConverter {

    /**
     * Converter of a grade value to a different grading system.
     *
     * @param gradeValue Original grade value
     * @param targetGradingSystemId Target grading system
     * @return grade value for target grading system
     * @throws GradeException if conversion fails
     */
    GradeValue convert(GradeValue gradeValue, String targetGradingSystemId) throws GradeException;
}
