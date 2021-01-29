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

