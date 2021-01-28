package com.dryxtech.grade.grader;

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
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
public abstract class AbstractGrader {

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
        Objects.requireNonNull(gradingSystem, "numeric value must not be null");
        return gradingSystem.getTextValue(new BigDecimal(numericValue.toString()))
                .orElseThrow(() -> new GradeException(String.format("failed to get textValue for numericValue %s from grading system %s",
                        numericValue, gradingSystem.getId())));
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

    @Override
    public String toString() {
        return String.format("grader{gradingSystem=\"%s\"}", gradingSystem);
    }
}
