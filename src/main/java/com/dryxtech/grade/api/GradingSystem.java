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
import java.util.Collection;
import java.util.Optional;

/**
 * This interface represents a grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public interface GradingSystem {

    /**
     * Getter of identification for grading system.
     *
     * @return grading system id
     */
    String getId();

    /**
     * Getter of description for grading system.
     *
     * @return grading system description
     */
    String getDescription();

    /**
     * Getter of category for grading system.
     *
     * @return grading system category
     */
    String getCategory();

    /**
     * Getter of type for grading system.
     *
     * @return grading system type
     */
    String getType();

    /**
     * Getter of name for grading system.
     *
     * @return grading system name
     */
    String getName();

    /**
     * Getter of variant for grading system.
     *
     * @return grading system variant
     */
    String getVariant();

    /**
     * Method to get the start value for the grading system range based on text value input.
     *
     * @param textValue text value for a range
     * @return optional containing start value of range; otherwise empty
     */
    Optional<BigDecimal> getStartNumericValue(String textValue);

    /**
     * Method to get the mid value for the grading system range based on text value input.
     *
     * @param textValue text value for a range
     * @return optional containing mid value of range; otherwise empty
     */
    Optional<BigDecimal> getMidNumericValue(String textValue);

    /**
     * Method to get the end value for the grading system range based on text value input.
     *
     * @param textValue text value for a range
     * @return optional containing end value of range; otherwise empty
     */
    Optional<BigDecimal> getEndNumericValue(String textValue);

    /**
     * Method to get the text value for a grading system range based on numeric value input.
     *
     * @param numericValue a valid numeric value within a range
     * @return optional containing text value of range; otherwise empty if not found
     */
    Optional<String> getTextValue(BigDecimal numericValue);

    /**
     * Method to get the performance level of a grading system range based on numeric value input.
     *
     * @param numericValue a valid numeric value within a range
     * @return optional containing performance level of range; otherwise empty if not found
     */
    Optional<PerformanceLevel> getPerformanceLevel(BigDecimal numericValue);

    /**
     * Getter of all grade value ranges for the grading system.
     *
     * @return collection of grade value ranges
     */
    Collection<GradeValueRange> getRanges();
}
