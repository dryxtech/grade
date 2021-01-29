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

package com.dryxtech.grade.system.converter;

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;

import java.math.BigDecimal;
import java.util.Objects;

public class TextValueBasedConverter implements GradeConverter {

    private final GradingSystemRegistry registry;

    public TextValueBasedConverter(GradingSystemRegistry registry) {
        Objects.requireNonNull(registry, "registry must not be null");
        this.registry = registry;
    }

    @Override
    public GradeValue convert(GradeValue gradeValue, String targetGradingSystemId) throws GradeException {

        GradingSystem targetGradingSystem = registry.lookupSystem(targetGradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(targetGradingSystemId));

        BigDecimal startNumericValue = targetGradingSystem.getStartNumericValue(gradeValue.getTextValue())
                .orElseThrow(() -> new GradeException(String.format("failed to get startNumericValue for textValue %s from target grading system %s",
                        gradeValue.getTextValue(), targetGradingSystem.getId())));

        BigDecimal midNumericValue = targetGradingSystem.getMidNumericValue(gradeValue.getTextValue())
                .orElseThrow(() -> new GradeException(String.format("failed to get midNumericValue for textValue %s from target grading system %s",
                        gradeValue.getTextValue(), targetGradingSystem.getId())));

        BigDecimal endNumericValue = targetGradingSystem.getEndNumericValue(gradeValue.getTextValue())
                .orElseThrow(() -> new GradeException(String.format("failed to get endNumericValue for textValue %s from target grading system %s",
                        gradeValue.getTextValue(), targetGradingSystem.getId())));

        BigDecimal numericValue = gradeValue.getNumericValue();
        if ((gradeValue.getNumericValue().compareTo(startNumericValue) < 0) ||
                (gradeValue.getNumericValue().compareTo(endNumericValue) > 0)) {
            numericValue = midNumericValue;
        }

        return GradeValueBuilder.builder()
                .gradingSystem(targetGradingSystem.getId())
                .numericValue(numericValue)
                .textValue(gradeValue.getTextValue())
                .build();
    }
}

