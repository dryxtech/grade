package com.dryxtech.grade.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeReference;
import com.dryxtech.grade.api.GradeValue;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A basic implementation of a Grade
 *
 * @author Drew Griffin
 * @since 1.0
 */
@JsonPropertyOrder({"id", "type", "timestamp", "description", "extensions", "weight", "numericValue", "textValue",
        "gradingSystem", "references"})
public class BasicGrade implements Grade {

    protected final String id;
    protected final String type;
    protected final String description;
    protected final Map<String, Object> extensions;
    protected final BigDecimal numericValue;
    protected final String textValue;
    protected final String gradingSystem;
    protected final ZonedDateTime timestamp;
    protected final BigDecimal weight;
    protected final Map<String, GradeReference> references;

    @JsonCreator
    public BasicGrade(@JsonProperty("id") String id, @JsonProperty("type") String type, @JsonProperty("description") String description,
                      @JsonProperty("extensions") Map<String, Object> extensions, @JsonProperty("numericValue") BigDecimal numericValue,
                      @JsonProperty("textValue") String textValue, @JsonProperty("gradingSystem") String gradingSystem,
                      @JsonProperty("timeStamp") ZonedDateTime timestamp, @JsonProperty("weight") BigDecimal weight,
                      @JsonProperty("references") Map<String, BasicGradeReference> references) {
        this(new BasicGradeReference(id, type, description, extensions),
                new BasicGradeValue(numericValue, textValue, gradingSystem), timestamp, weight, references);
    }

    public BasicGrade(final GradeReference selfReference, final GradeValue gradeValue, final ZonedDateTime timestamp,
                      final BigDecimal weight, final Map<String, ? extends GradeReference> references) {

        Objects.requireNonNull(selfReference, "grade self reference must not be null");
        Objects.requireNonNull(gradeValue, "grade value must not be null");

        this.id = selfReference.getId();
        this.type = selfReference.getType();
        this.description = selfReference.getDescription();
        if (Objects.nonNull(selfReference.getExtensions())) {
            this.extensions = Collections.unmodifiableMap(selfReference.getExtensions());
        } else {
            this.extensions = Collections.unmodifiableMap(new HashMap<>());
        }
        this.numericValue = gradeValue.getNumericValue();
        this.textValue = gradeValue.getTextValue();
        this.gradingSystem = gradeValue.getGradingSystem();
        this.timestamp = timestamp;

        if (Objects.isNull(weight)) {
            this.weight = BigDecimal.ONE;
        } else {
            this.weight = weight;
        }

        if (Objects.nonNull(references)) {
            this.references = Collections.unmodifiableMap(references);
        } else {
            this.references = null;
        }
    }

    @Override
    public String getGradingSystem() {
        return gradingSystem;
    }

    @Override
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public Optional<GradeReference> getReference(String key) {
        if (Objects.isNull(key) || Objects.isNull(references)) {
            return Optional.empty();
        }

        return Optional.ofNullable(references.get(key));
    }

    @Override
    public BigDecimal getWeight() {
        return weight;
    }

    @Override
    public Map<String, GradeReference> getReferences() {
        if (Objects.nonNull(references)) {
            return new HashMap<>(references);
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Optional<Object> getExtension(String key) {
        return Optional.ofNullable(extensions.get(key));
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
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
    public int compareTo(GradeValue o) {
        return new BasicGradeValue(numericValue, textValue, gradingSystem).compareTo(o);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Grade)) {
            return false;
        }

        Grade grade = (Grade) o;

        long thisEpochTime = Objects.nonNull(getTimestamp()) ? getTimestamp().toEpochSecond() : 0;
        long thatEpochTime = Objects.nonNull(grade.getTimestamp()) ? grade.getTimestamp().toEpochSecond() : 0;

        return Objects.equals(this.getId(), grade.getId()) &&
                Objects.equals(this.getType(), grade.getType()) &&
                Objects.equals(this.getNumericValue(), grade.getNumericValue()) &&
                Objects.equals(this.getTextValue(), grade.getTextValue()) &&
                Objects.equals(this.getGradingSystem(), grade.getGradingSystem()) &&
                Objects.equals(thisEpochTime, thatEpochTime) &&
                Objects.equals(this.getDescription(), grade.getDescription()) &&
                Objects.equals(this.getExtensions(), grade.getExtensions()) &&
                Objects.equals(this.getReferences(), grade.getReferences()) &&
                Objects.equals(this.getWeight(), grade.getWeight());
    }

    @Override
    public int hashCode() {
        long epochTime = Objects.nonNull(timestamp) ? getTimestamp().toEpochSecond() : 0;
        return Objects.hash(getId(), getType(), getNumericValue(), getTextValue(), getGradingSystem(), epochTime,
                getWeight(), getDescription(), getExtensions(), getReferences());
    }

    @Override
    public String toString() {
        return "BasicGrade{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", extensions=" + extensions +
                ", numericValue=" + numericValue +
                ", textValue='" + textValue + '\'' +
                ", gradingSystem='" + gradingSystem + '\'' +
                ", timestamp=" + timestamp +
                ", weight=" + weight +
                ", references=" + references +
                '}';
    }
}
