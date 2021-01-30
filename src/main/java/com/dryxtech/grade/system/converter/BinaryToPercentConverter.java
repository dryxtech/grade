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

public class BinaryToPercentConverter implements GradeConverter {

    private final GradingSystemRegistry registry;

    public BinaryToPercentConverter(GradingSystemRegistry registry) {
        Objects.requireNonNull(registry, "registry must not be null");
        this.registry = registry;
    }

    @Override
    public GradeValue convert(GradeValue gradeValue, String targetGradingSystemId) throws GradeException {

        final BigDecimal newNumericValue;
        if (gradeValue.getNumericValue().equals(BigDecimal.ONE)) {
            newNumericValue = new BigDecimal(100);
        } else {
            newNumericValue = BigDecimal.ONE;
        }

        GradingSystem targetGradingSystem = registry.lookupSystem(targetGradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(targetGradingSystemId));

        String textValue = targetGradingSystem.getTextValue(newNumericValue)
                .orElseThrow(() -> new GradeException(String.format("failed to get textValue for numericValue %s from target grading system %s",
                        newNumericValue, targetGradingSystem.getId())));

        return GradeValueBuilder.builder()
                .gradingSystem(targetGradingSystem.getId())
                .numericValue(newNumericValue)
                .textValue(textValue)
                .build();
    }
}
