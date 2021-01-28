package com.dryxtech.grade.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.dryxtech.grade.api.GradeValueRange;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.api.PerformanceLevel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A basic implementation of a Grading System
 *
 * @author Drew Griffin
 * @since 1.0
 */
@JsonPropertyOrder({"id", "description", "category", "type", "name", "variant", "ranges"})
public class BasicGradingSystem implements GradingSystem {

    protected final String id;
    protected final String description;
    protected final String category;
    protected final String type;
    protected final String name;
    protected final String variant;
    protected List<GradeValueRange> ranges;
    protected transient Map<String, GradeValueRange> rangesByTextValue;

    @JsonCreator
    public BasicGradingSystem(@JsonProperty("id") String id,
                              @JsonProperty("description") String description,
                              @JsonProperty("category") String category,
                              @JsonProperty("type") String type,
                              @JsonProperty("name") String name,
                              @JsonProperty("variant") String variant,
                              @JsonProperty("ranges") Collection<BasicGradeValueRange> ranges) {
        this.id = id;
        this.description = description;
        this.category = category;
        this.type = type;
        this.name = name;
        this.variant = variant;
        if (Objects.nonNull(ranges)) {
            setRanges(ranges.stream().sorted().collect(Collectors.toList()));
        } else {
            setRanges(new ArrayList<>());
        }
    }

    protected void setRanges(Collection<GradeValueRange> ranges) {
        this.ranges = new ArrayList<>(ranges);
        this.rangesByTextValue = new HashMap<>();
        ranges.forEach(range -> this.rangesByTextValue.put(range.getTextValue().toUpperCase(), range));
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getVariant() {
        return variant;
    }

    @Override
    public Collection<GradeValueRange> getRanges() {
        return ranges.stream().map(r -> new BasicGradeValueRange(r.getTextValue(),
                r.getStartValue(), r.getEndValue(), r.getStartValueInclusive(), r.getEndValueInclusive(),
                r.getPerformanceLevel().getLabel()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BigDecimal> getStartNumericValue(String textValue) {
        return getRange(textValue).map(GradeValueRange::getEffectiveRangeStartValue);
    }

    @Override
    public Optional<BigDecimal> getMidNumericValue(String textValue) {
        return getRange(textValue).map(GradeValueRange::getEffectiveRangeMidValue);
    }

    @Override
    public Optional<BigDecimal> getEndNumericValue(String textValue) {
        return getRange(textValue).map(GradeValueRange::getEffectiveRangeEndValue);
    }

    @Override
    public Optional<String> getTextValue(final BigDecimal numericValue) {
        if (Objects.nonNull(ranges)) {
            return ranges.stream()
                    .filter(range -> range.inRange(numericValue))
                    .map(GradeValueRange::getTextValue)
                    .findFirst();
        }
        return Optional.empty();
    }

    @Override
    public Optional<PerformanceLevel> getPerformanceLevel(BigDecimal numericValue) {
        if (Objects.nonNull(ranges)) {
            return ranges.stream()
                    .filter(range -> range.inRange(numericValue))
                    .map(GradeValueRange::getPerformanceLevel)
                    .findFirst();
        }
        return Optional.empty();
    }

    public Optional<GradeValueRange> getRange(String textValue) {
        if (Objects.nonNull(rangesByTextValue)) {
            return Optional.ofNullable(rangesByTextValue.get(textValue.toUpperCase()));
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicGradingSystem that = (BasicGradingSystem) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(description, that.description) &&
                Objects.equals(category, that.category) &&
                Objects.equals(type, that.type) &&
                Objects.equals(name, that.name) &&
                Objects.equals(variant, that.variant) &&
                Objects.equals(ranges, that.ranges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, category, type, name, variant, ranges);
    }

    @Override
    public String toString() {
        return "BasicGradingSystem{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", variant='" + variant + '\'' +
                ", ranges=" + ranges +
                '}';
    }
}
