package com.dryxtech.grade.grader;

import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeValueBuilder;
import com.dryxtech.grade.system.GradingSystemRegistry;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Grader class that determines grade value by grader's grading system.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class NumberGrader extends AbstractGrader implements Grader<Number> {

    public NumberGrader(final GradingSystem gradingSystem) {
        super(gradingSystem);
    }

    public NumberGrader(final GradingSystem gradingSystem, final GradingSystemRegistry registry) {
        super(gradingSystem, registry);
    }

    @Override
    public GradeValue grade(final Number numericValue) throws GradeException {
        Objects.requireNonNull(numericValue, "numeric value must not be null");

        String textValue = getTextValue(new BigDecimal(numericValue.toString()));

        return GradeValueBuilder.builder()
                .gradingSystem(gradingSystem.getId())
                .numericValue(numericValue)
                .textValue(textValue)
                .build();
    }
}
