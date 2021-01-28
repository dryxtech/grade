package com.dryxtech.grade.model;

import com.dryxtech.grade.util.GradeMathUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.dryxtech.grade.api.GradeValueRange;
import com.dryxtech.grade.api.PerformanceLevel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * A class for representing a range of grade values. used in grading systems
 *
 * @author Drew Griffin
 * @since 1.0
 */
@JsonPropertyOrder({"textValue", "startValue", "startValueInclusive", "endValue", "endValueInclusive"})
public class BasicGradeValueRange implements GradeValueRange, Comparable<GradeValueRange> {

    public static final BigDecimal NANO_UNIT = new BigDecimal("0.000000001");

    private final String textValue;
    private final BigDecimal startValue;
    private final BigDecimal endValue;
    private final boolean startValueInclusive;
    private final boolean endValueInclusive;
    private final PerformanceLevel performanceLevel;
    private final BigDecimal effectiveRangeStartValue;
    private final BigDecimal effectiveRangeMidValue;
    private final BigDecimal effectiveRangeEndValue;

    @JsonCreator
    public BasicGradeValueRange(@JsonProperty("textValue") String textValue,
                                @JsonProperty("startValue") BigDecimal startValue,
                                @JsonProperty("endValue") BigDecimal endValue,
                                @JsonProperty("startValueInclusive") boolean startValueInclusive,
                                @JsonProperty("endValueInclusive") boolean endValueInclusive,
                                @JsonProperty("performanceLevel") String performanceLevel) {

        this.textValue = Objects.toString(textValue, "");
        this.startValue = Objects.requireNonNull(startValue, "startRangeInclusive must not be null");
        this.endValue = Objects.requireNonNull(endValue, "endRangeInclusive must not be null");
        this.startValueInclusive = startValueInclusive;
        this.endValueInclusive = endValueInclusive;
        this.performanceLevel = PerformanceLevel.of(performanceLevel);

        this.effectiveRangeStartValue = (this.startValueInclusive) ? this.startValue : this.startValue.add(NANO_UNIT);
        this.effectiveRangeEndValue = (this.endValueInclusive) ? this.endValue : this.endValue.subtract(NANO_UNIT);

        if (effectiveRangeStartValue.compareTo(effectiveRangeEndValue) > 0) {
            throw new IllegalArgumentException("effective start value must not be greater than effective end value");
        }

        if (effectiveRangeStartValue.compareTo(effectiveRangeEndValue) == 0) {
            this.effectiveRangeMidValue = effectiveRangeStartValue;
        } else {
            this.effectiveRangeMidValue = GradeMathUtil.calculateAverage(
                    Arrays.asList(this.effectiveRangeStartValue, this.effectiveRangeEndValue), BigDecimal.ZERO);
        }
    }

    @Override
    public String getTextValue() {
        return textValue;
    }

    @Override
    public BigDecimal getStartValue() {
        return startValue;
    }

    @Override
    public BigDecimal getEndValue() {
        return endValue;
    }

    @Override
    public boolean getStartValueInclusive() {
        return startValueInclusive;
    }

    @Override
    public boolean getEndValueInclusive() {
        return endValueInclusive;
    }

    @Override
    public PerformanceLevel getPerformanceLevel() {
        return performanceLevel;
    }

    @JsonIgnore
    public BigDecimal getEffectiveRangeStartValue() {
        return effectiveRangeStartValue;
    }

    @JsonIgnore
    public BigDecimal getEffectiveRangeMidValue() {
        return effectiveRangeMidValue;
    }

    @JsonIgnore
    public BigDecimal getEffectiveRangeEndValue() {
        return effectiveRangeEndValue;
    }

    public boolean inRange(final BigDecimal value) {
        return compareValue(value) == 0;
    }

    public int compareValue(final BigDecimal value) {

        if (Objects.isNull(value)) {
            return -1;
        }

        if ((getStartValueInclusive() && (value.compareTo(startValue) < 0)) ||
                (!getStartValueInclusive() && (value.compareTo(startValue) <= 0))) {
            return -1;
        }

        if ((getEndValueInclusive() && (value.compareTo(endValue) > 0)) ||
                (!getEndValueInclusive() && (value.compareTo(endValue) >= 0))) {
            return 1;
        }

        return 0;
    }

    @Override
    public int compareTo(GradeValueRange range) {
        return compareValue(range.getEffectiveRangeStartValue());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        } else if (o instanceof GradeValueRange) {
            BasicGradeValueRange nnr = (BasicGradeValueRange) o;
            return Objects.equals(getTextValue(), nnr.getTextValue()) &&
                    Objects.equals(getStartValueInclusive(), nnr.getStartValueInclusive()) &&
                    Objects.equals(getEndValueInclusive(), nnr.getEndValueInclusive()) &&
                    Objects.equals(getStartValue(), nnr.getStartValue()) &&
                    Objects.equals(getEndValue(), nnr.getEndValue()) &&
                    Objects.equals(getPerformanceLevel(), nnr.getPerformanceLevel());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(textValue, startValueInclusive, endValueInclusive, startValue, endValue, performanceLevel);
    }

    @Override
    public String toString() {
        return "BasicGradeValueRange{" +
                "textValue='" + textValue + '\'' +
                ", startValue=" + startValue +
                ", endValue=" + endValue +
                ", startValueInclusive=" + startValueInclusive +
                ", endValueInclusive=" + endValueInclusive +
                ", performanceLevel=" + performanceLevel +
                ", effectiveRangeStartValue=" + effectiveRangeStartValue +
                ", effectiveRangeMidValue=" + effectiveRangeMidValue +
                ", effectiveRangeEndValue=" + effectiveRangeEndValue +
                '}';
    }
}

