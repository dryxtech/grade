package com.dryxtech.grade.model;

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.ManagedGrade;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public class BasicManagedGrade extends BasicGrade implements ManagedGrade {

    protected final Map<String, Object> management;

    @JsonCreator
    public BasicManagedGrade(@JsonProperty("id") String id,
                             @JsonProperty("type") String type,
                             @JsonProperty("description") String description,
                             @JsonProperty("extensions") Map<String, Object> extensions,
                             @JsonProperty("numericValue") BigDecimal numericValue,
                             @JsonProperty("textValue") String textValue,
                             @JsonProperty("gradingSystem") String gradingSystem,
                             @JsonProperty("timeStamp") ZonedDateTime timestamp,
                             @JsonProperty("weight") BigDecimal weight,
                             @JsonProperty("references") Map<String, BasicGradeReference> references,
                             @JsonProperty("management") Map<String, Object> management) {
        super(id, type, description, extensions, numericValue, textValue, gradingSystem, timestamp, weight, references);
        this.management = management;
    }

    public BasicManagedGrade(Grade grade, Map<String, Object> management) {
        super(grade, grade, grade.getTimestamp(), grade.getWeight(), grade.getReferences());
        this.management = management;
    }

    public Map<String, Object> getManagement() {
        return management;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getNumericValue(), getTextValue(), getGradingSystem(), getTimestamp(),
                getWeight(), getDescription(), getExtensions(), getReferences(), management);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof ManagedGrade) {
            return Objects.equals(management, ((ManagedGrade) o).getManagement()) && super.equals(o);
        }

        return super.equals(o);
    }
}
