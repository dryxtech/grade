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
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * This interface represents a test score.
 *
 * @author Drew Griffin
 * @since 2021.2
 */
public interface TestScore extends TestReference {

    /**
     * Getter of test score timestamp.
     *
     * @return Timestamp of test score
     */
    ZonedDateTime getTimestamp();

    /**
     * Getter of test score weighted value
     *
     * @return test score weighted value
     */
    BigDecimal getValue();

    /**
     * Getter of score per individual question.
     *
     * @return individual question scores
     */
    Map<Question, BigDecimal> getQuestionScores();

    /**
     * Getter for reference of test submission.
     *
     * @param key reference lookup name
     * @return A references to a test submission related item
     */
    Optional<TestReference> getReference(String key);

    /**
     * Getter for all test references.
     *
     * @return All references for a test submission
     */
    Map<String, TestReference> getReferences();
}
