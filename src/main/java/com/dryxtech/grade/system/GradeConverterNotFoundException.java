package com.dryxtech.grade.system;

import com.dryxtech.grade.api.GradeException;

/**
 * An exception class for when grade converter is not found
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeConverterNotFoundException extends GradeException {

    private final String fromGradingSystemId;
    private final String toGradingSystemId;

    public GradeConverterNotFoundException(String fromGradingSystemId, String toGradingSystemId) {
        super("grade converter not found");
        this.fromGradingSystemId = fromGradingSystemId;
        this.toGradingSystemId = toGradingSystemId;
    }

    public String getFromGradingSystemId() {
        return fromGradingSystemId;
    }

    public String getToGradingSystemId() {
        return toGradingSystemId;
    }
}
