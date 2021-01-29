package com.dryxtech.grade.model;

import com.dryxtech.grade.api.GradeReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A basic implementation of a Grade Reference
 *
 * @author Drew Griffin
 * @since 1.0
 */
@JsonPropertyOrder({"id", "type", "description", "extensions"})
public class BasicGradeReference implements GradeReference {

    private final String id;
    private final String type;
    private final String description;
    private final Map<String, Object> extensions;

    @JsonCreator
    public BasicGradeReference(@JsonProperty("id") String id,
                               @JsonProperty("type") String type,
                               @JsonProperty("description") String description,
                               @JsonProperty("extensions") Map<String, Object> extensions) {

        this.id = Objects.requireNonNull(id, "grade reference id must not be null");
        this.type = Objects.requireNonNull(type, "grade reference type must not be null");
        this.description = Objects.toString(description, "");

        if (Objects.nonNull(extensions)) {
            this.extensions = Collections.unmodifiableMap(extensions);
        } else {
            this.extensions = null;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, description, extensions);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof GradeReference)) {
            return false;
        }

        GradeReference ref = (GradeReference) o;

        return Objects.equals(this.getId(), ref.getId()) &&
                Objects.equals(this.getType(), ref.getType()) &&
                Objects.equals(this.getDescription(), ref.getDescription()) &&
                Objects.equals(this.getExtensions(), ref.getExtensions());
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
        if (Objects.isNull(key) || Objects.isNull(extensions)) {
            return Optional.empty();
        }

        return Optional.ofNullable(extensions.get(key));
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public String toString() {
        return "BasicGradeReference{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", extensions=" + extensions +
                '}';
    }
}
