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

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.system.GradingSystemRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * An Abstract Class for graders.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public abstract class AbstractGrader<T> implements Grader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractGrader.class);

    protected final GradingSystem gradingSystem;
    protected final GradingSystemRegistry registry;

    protected AbstractGrader(final GradingSystem gradingSystem) {
        Objects.requireNonNull(gradingSystem, "grading system must not be null");
        this.gradingSystem = gradingSystem;
        this.registry = null;
    }

    protected AbstractGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        Objects.requireNonNull(gradingSystem, "grading system must not be null");
        this.gradingSystem = gradingSystem;
        this.registry = registry;
    }

    public String getTextValue(final BigDecimal numericValue) throws GradeException {
        Objects.requireNonNull(numericValue, "numeric value must not be null");
        return gradingSystem.getTextValue(new BigDecimal(numericValue.toString()))
                .orElseThrow(() -> new GradeException(String.format("failed to get textValue for numericValue %s from grading system %s",
                        numericValue, gradingSystem.getId())));
    }

    public GradeValue getGradeValue(final BigDecimal numericValue) throws GradeException {
        Objects.requireNonNull(numericValue, "numeric value must not be null");
        return GradeValueBuilder.builder()
                .gradingSystem(gradingSystem.getId())
                .numericValue(numericValue)
                .textValue(getTextValue(numericValue))
                .build();
    }

    protected GradeValue convert(final GradeValue gradeValue) {

        if (!gradeValue.getGradingSystem().equals(this.gradingSystem.getId()) && Objects.nonNull(registry)) {
            Optional<GradeConverter> converter = registry.lookupConverter(gradeValue.getGradingSystem(), gradingSystem.getId());
            if (converter.isPresent()) {
                try {
                    return converter.get().convert(gradeValue, gradingSystem.getId());
                } catch (GradeException e) {
                    LOGGER.warn("failed to convert value. reason: {}", e.getMessage());
                }
            }
        }
        return gradeValue;
    }

    public abstract GradeValue grade(T t) throws GradeException;

    @Override
    public String toString() {
        return String.format("grader{gradingSystem=\"%s\"}", gradingSystem);
    }
}
