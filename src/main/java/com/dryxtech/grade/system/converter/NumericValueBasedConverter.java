package com.dryxtech.grade.system.converter;

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;

import java.util.Objects;

public class NumericValueBasedConverter implements GradeConverter {

    private final GradingSystemRegistry registry;

    public NumericValueBasedConverter(GradingSystemRegistry registry) {
        Objects.requireNonNull(registry, "registry must not be null");
        this.registry = registry;
    }

    @Override
    public GradeValue convert(GradeValue gradeValue, String targetGradingSystemId) throws GradeException {

        GradingSystem targetGradingSystem = registry.lookupSystem(targetGradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(targetGradingSystemId));

        String textValue = targetGradingSystem.getTextValue(gradeValue.getNumericValue())
                .orElseThrow(() -> new GradeException(String.format("failed to get textValue for numericValue %s from target grading system %s",
                        gradeValue.getNumericValue(), targetGradingSystem.getId())));

        return GradeValueBuilder.builder()
                .gradingSystem(targetGradingSystem.getId())
                .numericValue(gradeValue.getNumericValue())
                .textValue(textValue)
                .build();
    }
}
