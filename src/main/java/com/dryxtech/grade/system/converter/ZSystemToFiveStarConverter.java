package com.dryxtech.grade.system.converter;

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.system.GradingSystemType;
import com.dryxtech.grade.system.ZGradingSystem;
import com.dryxtech.grade.system.ZLevel;

import java.util.Objects;

public class ZSystemToFiveStarConverter implements GradeConverter {

    private static final GradingSystem Z_SYSTEM = new ZGradingSystem();

    private final GradingSystemRegistry registry;

    protected ZSystemToFiveStarConverter(GradingSystemRegistry registry) {
        Objects.requireNonNull(registry, "registry must not be null");
        this.registry = registry;
    }

    @Override
    public GradeValue convert(GradeValue gradeValue, String targetGradingSystemId) throws GradeException {

        if (!gradeValue.getGradingSystem().equals(Z_SYSTEM.getId())) {
            throw new IllegalArgumentException("source value's grading system must be " + Z_SYSTEM.getId());
        }

        GradingSystem targetGradingSystem = registry.lookupSystem(targetGradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(targetGradingSystemId));

        if (!targetGradingSystem.getType().equals(GradingSystemType.RATING.getLabel())) {
            throw new IllegalArgumentException("target grading system must be of type " + GradingSystemType.RATING.getLabel());
        }

        switch (ZLevel.of(gradeValue.getTextValue())
                .orElseThrow(() -> new GradeException("invalid grade text value for grading system " + GradeConstants.Z_GRADING_SYSTEM))) {

            case TOP_PLUS:
            case TOP:
            case TOP_MINUS:
                return GradeValueBuilder.builder().gradingSystem(targetGradingSystemId).textValue("5-STAR")
                        .numericValue(targetGradingSystem.getEndNumericValue("5-STAR")
                                .orElseThrow(() -> new GradeException("5-STAR range not found"))).build();

            case HIGH_PLUS:
            case HIGH:
            case HIGH_MINUS:
                return GradeValueBuilder.builder().gradingSystem(targetGradingSystemId).textValue("4-STAR")
                        .numericValue(targetGradingSystem.getEndNumericValue("4-STAR")
                                .orElseThrow(() -> new GradeException("4-STAR range not found"))).build();

            case MEDIUM_PLUS:
            case MEDIUM:
            case MEDIUM_MINUS:
                return GradeValueBuilder.builder().gradingSystem(targetGradingSystemId).textValue("3-STAR")
                        .numericValue(targetGradingSystem.getEndNumericValue("3-STAR")
                                .orElseThrow(() -> new GradeException("3-STAR range not found"))).build();

            case LOW_PLUS:
            case LOW:
            case LOW_MINUS:
                return GradeValueBuilder.builder().gradingSystem(targetGradingSystemId).textValue("2-STAR")
                        .numericValue(targetGradingSystem.getEndNumericValue("2-STAR")
                                .orElseThrow(() -> new GradeException("2-STAR range not found"))).build();

            case BOTTOM_PLUS:
            case BOTTOM:
            case BOTTOM_MINUS:
                return GradeValueBuilder.builder().gradingSystem(targetGradingSystemId).textValue("1-STAR")
                        .numericValue(targetGradingSystem.getEndNumericValue("1-STAR")
                                .orElseThrow(() -> new GradeException("1-STAR range not found"))).build();

            default:
                throw new GradeException("unsupported conversion for grade value range");
        }
    }
}
