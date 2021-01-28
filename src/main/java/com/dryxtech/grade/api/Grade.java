package com.dryxtech.grade.api;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents a grade.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface Grade extends GradeValue, GradeReference {

    /**
     * Getter of grade timestamp.
     * @return Timestamp of grade
     */
    ZonedDateTime getTimestamp();

    /**
     * Getter of grade weight.
     * @return Weight of grade
     */
    BigDecimal getWeight();

    /**
     * Getter for grade reference.
     *
     * @param key reference lookup name
     * @return A references to a grade related item
     */
    Optional<GradeReference> getReference(String key);

    /**
     * Getter for all grade references.
     * @return All references for a grade
     */
    Map<String, GradeReference> getReferences();
}
