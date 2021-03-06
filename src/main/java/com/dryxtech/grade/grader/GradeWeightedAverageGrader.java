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

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.util.GradeMathUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/**
 * Grader class that determines grade value using weighted average of grade collection and grader's grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeWeightedAverageGrader extends AbstractGrader<Collection<Grade>> {

    public GradeWeightedAverageGrader(final GradingSystem gradingSystem) {
        super(gradingSystem);
    }

    public GradeWeightedAverageGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        super(gradingSystem, registry);
    }

    @Override
    public GradeValue grade(final Collection<Grade> grades) throws GradeException {
        Objects.requireNonNull(grades, "grades must not be null");

        BigDecimal numericValue = GradeMathUtil.calculateWeightedAverage(grades)
                .orElseThrow(() -> new GradeException("failed to get weighted average of grades"));

        return getGradeValue(numericValue);
    }
}

