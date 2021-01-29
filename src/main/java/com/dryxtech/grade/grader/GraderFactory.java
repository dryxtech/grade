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
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;

import java.util.Collection;
import java.util.Objects;

/**
 * A Class used for creating graders based on input type.
 * Constructed with registry for looking up grading systems;
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GraderFactory {

    private final GradingSystemRegistry registry;

    public GraderFactory(final GradingSystemRegistry registry) {
        Objects.requireNonNull(registry, "grading system registry must not be null");

        this.registry = registry;
    }

    public Grader<Number> createNumberGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new NumberGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }

    public Grader<Collection<Number>> createNumberAverageGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new NumberAverageGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }

    public Grader<Collection<GradeValue>> createGradeValueAverageGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new GradeValueAverageGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }

    public Grader<Collection<Grade>> createGradeWeightedAverageGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new GradeWeightedAverageGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }
}
