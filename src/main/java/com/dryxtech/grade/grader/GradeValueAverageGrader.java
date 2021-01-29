package com.dryxtech.grade.grader;

import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.util.GradeMathUtil;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Grader class that determines grade value using average of grade collection and grader's grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeValueAverageGrader extends AbstractGrader<Collection<GradeValue>> {

    public GradeValueAverageGrader(final GradingSystem gradingSystem) {
        super(gradingSystem);
    }

    public GradeValueAverageGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        super(gradingSystem, registry);
    }

    @Override
    public GradeValue grade(final Collection<GradeValue> values) throws GradeException {
        Objects.requireNonNull(values, "grade values must not be null");

        BigDecimal numericValue = GradeMathUtil.calculateAverage(values.stream()
                .map(this::convert)
                .map(GradeValue::getNumericValue)
                .collect(Collectors.toList()))
                .orElseThrow(() -> new GradeException("failed to get average of grade values"));

        String textValue = getTextValue(numericValue);

        return GradeValueBuilder.builder()
                .gradingSystem(gradingSystem.getId())
                .numericValue(numericValue)
                .textValue(textValue)
                .build();
    }
}
