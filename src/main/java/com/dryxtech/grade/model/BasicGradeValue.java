package com.dryxtech.grade.model;

import com.dryxtech.grade.api.GradeValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A basic implementation of a Grade Value
 *
 * @author Drew Griffin
 * @since 1.0
 */
@JsonPropertyOrder({"numericValue", "textValue", "gradingSystem"})
public class BasicGradeValue implements GradeValue {

    private final BigDecimal numericValue;
    private final String textValue;
    private final String gradingSystem;

    @JsonCreator
    public BasicGradeValue(@JsonProperty("numericValue") BigDecimal numericValue,
                           @JsonProperty("textValue") String textValue,
                           @JsonProperty("gradingSystem") String gradingSystem) {
        this.numericValue = Objects.requireNonNull(numericValue, "numericValue must not be null");
        this.textValue = Objects.toString(textValue, "");
        this.gradingSystem = Objects.requireNonNull(gradingSystem, "gradingSystem must not be null");
    }

    @Override
    public int hashCode() {
        return Objects.hash(Objects.toString(numericValue), textValue, gradingSystem);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof GradeValue)) {
            return false;
        }

        GradeValue gv = (GradeValue) o;

        return Objects.equals(this.getGradingSystem(), gv.getGradingSystem()) &&
                Objects.equals(this.getNumericValue(), gv.getNumericValue()) &&
                Objects.equals(this.getTextValue(), gv.getTextValue());
    }

    @Override
    public BigDecimal getNumericValue() {
        return numericValue;
    }

    @Override
    public String getTextValue() {
        return textValue;
    }

    @Override
    public String getGradingSystem() {
        return gradingSystem;
    }

    @Override
    public String toString() {
        return "BasicGradeValue{" +
                "numericValue=" + numericValue +
                ", textValue='" + textValue + '\'' +
                ", gradingSystem='" + gradingSystem + '\'' +
                '}';
    }

    @Override
    public int compareTo(final GradeValue gradeValue) {

        if (Objects.isNull(gradeValue)) {
            return -1;
        } else if (Objects.nonNull(this.getNumericValue()) && Objects.nonNull(gradeValue.getNumericValue())) {
            return this.getNumericValue().compareTo(gradeValue.getNumericValue());
        } else {
            return 0;
        }
    }
}
