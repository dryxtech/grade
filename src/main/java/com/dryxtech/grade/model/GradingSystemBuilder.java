package com.dryxtech.grade.model;

import com.dryxtech.grade.api.GradeValueRange;
import com.dryxtech.grade.api.GradingSystem;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * A builder class for basic grading systems
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradingSystemBuilder {

    private String id;
    private String description;
    private String category;
    private String type;
    private String name;
    private String variant;
    private Collection<GradeValueRange> ranges;

    public GradingSystemBuilder(final GradingSystem gradingSystem) throws IOException {

        this.id = gradingSystem.getId();
        this.description = gradingSystem.getDescription();
        this.category = gradingSystem.getCategory();
        this.type = gradingSystem.getType();
        this.name = gradingSystem.getName();
        this.variant = gradingSystem.getVariant();
        this.ranges = gradingSystem.getRanges();
    }

    public GradingSystemBuilder() {
    }

    public static GradingSystemBuilder builder() {
        return new GradingSystemBuilder();
    }

    public GradingSystemBuilder id(String id) {
        this.id = id;
        return this;
    }

    public GradingSystemBuilder description(String description) {
        this.description = description;
        return this;
    }

    public GradingSystemBuilder category(String category) {
        this.category = category;
        return this;
    }

    public GradingSystemBuilder type(String type) {
        this.type = type;
        return this;
    }

    public GradingSystemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GradingSystemBuilder variant(String variant) {
        this.variant = variant;
        return this;
    }

    public GradingSystemBuilder ranges(Collection<GradeValueRange> ranges) {
        this.ranges = ranges;
        return this;
    }

    public GradingSystem build() {

        BasicGradingSystem gradingSystem = new BasicGradingSystem(id, description, category, type, name, variant, ranges.stream().map(r -> new BasicGradeValueRange(r.getTextValue(),
                r.getStartValue(), r.getEndValue(), r.getStartValueInclusive(), r.getEndValueInclusive(), r.getPerformanceLevel().getLabel()))
                .collect(Collectors.toList()));
        clear();
        return gradingSystem;
    }

    public GradingSystemBuilder clear() {
        this.id = null;
        this.description = null;
        this.category = null;
        this.type = null;
        this.name = null;
        this.variant = null;
        this.ranges = null;
        return this;
    }
}
