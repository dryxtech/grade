/*
 * Copyright (c) 2021 DRYXTECH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dryxtech.grade.api;

import java.math.BigDecimal;

/**
 * This interface represents a named value range for a grade.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface GradeValueRange {

    /**
     * Getter of text value for range.
     *
     * @return text value
     */
    String getTextValue();

    /**
     * Getter of starting numeric value for range.
     *
     * @return start value
     */
    BigDecimal getStartValue();

    /**
     * Getter of ending numeric value for range.
     *
     * @return end value
     */
    BigDecimal getEndValue();

    /**
     * Getter of if start numeric value is inclusive.
     *
     * @return true if inclusive; false if exclusive
     */
    boolean getStartValueInclusive();

    /**
     * Getter of if end numeric value is inclusive.
     *
     * @return true if inclusive; false if exclusive
     */
    boolean getEndValueInclusive();

    /**
     * Getter of performance level of range.
     *
     * @return performance level
     */
    PerformanceLevel getPerformanceLevel();

    /**
     * Getter of effective start value (precision up to nano-second).
     *
     * @return effective start value
     */
    BigDecimal getEffectiveRangeStartValue();

    /**
     * Getter of effective mid value (determined by implementation).
     *
     * @return effective mid value
     */
    BigDecimal getEffectiveRangeMidValue();

    /**
     * Getter of effective end value (precision up to nano-second).
     *
     * @return effective end value
     */
    BigDecimal getEffectiveRangeEndValue();

    /**
     * Method to check if a numeric value is in this range.
     *
     * @param value to evaluate if in range
     * @return true if in range; otherwise false
     */
    boolean inRange(BigDecimal value);
}
