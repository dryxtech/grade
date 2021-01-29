package com.dryxtech.grade.api;

import java.math.BigDecimal;

/**
 * This interface represents a grade's value (both text and numeric)
 * and qualified by grading system used.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface GradeValue extends Comparable<GradeValue> {

    /**
     * Getter of grade numeric value.
     *
     * @return numeric value
     */
    BigDecimal getNumericValue();

    /**
     * Getter of grade text value.
     *
     * @return text value
     */
    String getTextValue();

    /**
     * Getter of grade grading-system.
     *
     * @return grading system id
     */
    String getGradingSystem();
}
