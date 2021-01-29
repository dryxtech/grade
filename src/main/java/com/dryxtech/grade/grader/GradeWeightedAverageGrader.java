package com.dryxtech.grade.grader;

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeValueBuilder;
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
public class GradeWeightedAverageGrader extends AbstractGrader implements Grader<Collection<Grade>> {

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

        String textValue = getTextValue(numericValue);

        return GradeValueBuilder.builder()
                .gradingSystem(gradingSystem.getId())
                .numericValue(numericValue)
                .textValue(textValue)
                .build();
    }
}

