package com.dryxtech.grade.grader;

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;

import java.util.Collection;
import java.util.Objects;

/**
 * A Class used for creating graders based on input type.
 * Constructed with registry for looking up grading systems;
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GraderFactory {

    private final GradingSystemRegistry registry;

    public GraderFactory(final GradingSystemRegistry registry) {
        Objects.requireNonNull(registry, "grading system registry must not be null");

        this.registry = registry;
    }

    public Grader<Number> createNumberGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new NumberGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }

    public Grader<Collection<Number>> createNumberAverageGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new NumberAverageGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }

    public Grader<Collection<GradeValue>> createGradeValueAverageGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new GradeValueAverageGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }

    public Grader<Collection<Grade>> createGradeWeightedAverageGrader(final String gradingSystemId) throws GradingSystemNotFoundException {
        return new GradeWeightedAverageGrader(registry.lookupSystem(gradingSystemId)
                .orElseThrow(() -> new GradingSystemNotFoundException(gradingSystemId)), registry);
    }
}
