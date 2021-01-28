package com.dryxtech.grade.system;

import com.dryxtech.grade.api.GradeException;

/**
 * An exception class for when grading system is not found
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradingSystemNotFoundException extends GradeException {

    private final String gradingSystemId;

    public GradingSystemNotFoundException(String gradingSystemId) {
        super("grading system not found. " + gradingSystemId);
        this.gradingSystemId = gradingSystemId;
    }

    public String getGradingSystemId() {
        return gradingSystemId;
    }
}
