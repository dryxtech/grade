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

package com.dryxtech.grade.grader;

import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.util.GradeMathUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/**
 * Grader class that determines grade value using average of number collection and grader's grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class NumberAverageGrader extends AbstractGrader<Collection<Number>> {

    public NumberAverageGrader(final GradingSystem gradingSystem) {
        super(gradingSystem);
    }

    public NumberAverageGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        super(gradingSystem, registry);
    }

    @Override
    public GradeValue grade(final Collection<Number> values) throws GradeException {
        Objects.requireNonNull(values, "values must not be null");

        BigDecimal numericValue = GradeMathUtil.calculateAverage(values)
                .orElseThrow(() -> new GradeException("failed to get average of number values"));

        return getGradeValue(numericValue);
    }
}
