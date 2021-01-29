package com.dryxtech.grade.model;

import com.dryxtech.grade.api.PerformanceLevel;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A builder class for basic grade value ranges
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeValueRangeBuilder {

    private String textValue;
    private BigDecimal rangeStartValue;
    private BigDecimal rangeEndValue;
    private boolean isRangeStartInclusive;
    private boolean isRangeEndInclusive;
    private String performanceLevel;

    public GradeValueRangeBuilder() {
        this.isRangeStartInclusive = true;
        this.isRangeEndInclusive = false;
    }

    public GradeValueRangeBuilder(String textValue, BigDecimal rangeStartValue, BigDecimal rangeEndValue,
                                  boolean isRangeStartInclusive, boolean isRangeEndInclusive, String performanceLevel) {
        this.textValue = textValue;
        this.rangeStartValue = rangeStartValue;
        this.rangeEndValue = rangeEndValue;
        this.isRangeStartInclusive = isRangeStartInclusive;
        this.isRangeEndInclusive = isRangeEndInclusive;
        this.performanceLevel = performanceLevel;
    }

    public static GradeValueRangeBuilder builder() {
        return new GradeValueRangeBuilder();
    }

    public GradeValueRangeBuilder textValue(final String textValue) {
        this.textValue = textValue;
        return this;
    }

    public GradeValueRangeBuilder rangeStartValue(final Number rangeStartValue) {
        if (Objects.nonNull(rangeStartValue)) {
            this.rangeStartValue = new BigDecimal(rangeStartValue.toString());
        } else {
            this.rangeStartValue = null;
        }
        return this;
    }

    public GradeValueRangeBuilder rangeEndValue(final Number rangeEndValue) {
        if (Objects.nonNull(rangeEndValue)) {
            this.rangeEndValue = new BigDecimal(rangeEndValue.toString());
        } else {
            this.rangeEndValue = null;
        }
        return this;
    }

    public GradeValueRangeBuilder isRangeStartInclusive(final boolean isRangeStartInclusive) {
        this.isRangeStartInclusive = isRangeStartInclusive;
        return this;
    }

    public GradeValueRangeBuilder isRangeEndInclusive(final boolean isRangeEndInclusive) {
        this.isRangeEndInclusive = isRangeEndInclusive;
        return this;
    }

    public GradeValueRangeBuilder performanceLevel(final PerformanceLevel performanceLevel) {
        this.performanceLevel = performanceLevel.getLabel();
        return this;
    }

    public GradeValueRangeBuilder performanceLevel(final String performanceLevel) {
        this.performanceLevel = performanceLevel;
        return this;
    }

    public BasicGradeValueRange build() {
        BasicGradeValueRange range = new BasicGradeValueRange(textValue, rangeStartValue, rangeEndValue,
                isRangeStartInclusive, isRangeEndInclusive, performanceLevel);
        clear();
        return range;
    }

    public GradeValueRangeBuilder clear() {
        this.textValue = null;
        this.rangeStartValue = null;
        this.rangeEndValue = null;
        this.isRangeStartInclusive = true;
        this.isRangeEndInclusive = false;
        this.performanceLevel = null;
        return this;
    }
}

