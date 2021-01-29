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
import java.util.stream.Collectors;

/**
 * Grader class that determines grade value using average of grade collection and grader's grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeValueAverageGrader extends AbstractGrader<Collection<GradeValue>> {

    public GradeValueAverageGrader(final GradingSystem gradingSystem) {
        super(gradingSystem);
    }

    public GradeValueAverageGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        super(gradingSystem, registry);
    }

    @Override
    public GradeValue grade(final Collection<GradeValue> values) throws GradeException {
        Objects.requireNonNull(values, "grade values must not be null");

        BigDecimal numericValue = GradeMathUtil.calculateAverage(values.stream()
                .map(this::convert)
                .map(GradeValue::getNumericValue)
                .collect(Collectors.toList()))
                .orElseThrow(() -> new GradeException("failed to get average of grade values"));

        return getGradeValue(numericValue);
    }
}
