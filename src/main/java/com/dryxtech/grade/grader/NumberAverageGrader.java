package com.dryxtech.grade.grader;

import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.util.GradeMathUtil;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.system.GradingSystemRegistry;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

/**
 * Grader class that determines grade value using average of number collection and grader's grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class NumberAverageGrader extends AbstractGrader implements Grader<Collection<Number>> {

    public NumberAverageGrader(final GradingSystem gradingSystem) {
        super(gradingSystem);
    }

    public NumberAverageGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        super(gradingSystem, registry);
    }

    @Override
    public GradeValue grade(final Collection<Number> values) throws GradeException {
        Objects.requireNonNull(values, "values must not be null");

        BigDecimal numericValue = GradeMathUtil.calculateAverage(values)
                .orElseThrow(() -> new GradeException("failed to get average of number values"));

        String textValue = getTextValue(numericValue);

        return GradeValueBuilder.builder()
                .gradingSystem(gradingSystem.getId())
                .numericValue(numericValue)
                .textValue(textValue)
                .build();
    }
}
