package com.dryxtech.grade.model;

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeReference;
import com.dryxtech.grade.api.GradeValue;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A builder class for basic grades
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeBuilder {

    protected final GradeReferenceBuilder gradeReferenceBuilder;
    protected final GradeValueBuilder gradeValueBuilder;
    protected ZonedDateTime timestamp;
    protected BigDecimal weight = BigDecimal.ONE;
    protected Map<String, BasicGradeReference> references;

    public GradeBuilder(Grade grade) {
        this.gradeReferenceBuilder = new GradeReferenceBuilder(grade.getId(), grade.getType(),
                grade.getDescription(), grade.getExtensions());
        this.gradeValueBuilder = new GradeValueBuilder(grade.getNumericValue(), grade.getTextValue(),
                grade.getGradingSystem());
        this.timestamp = grade.getTimestamp();
    }

    public GradeBuilder(boolean autoGenerateId) {
        this.gradeReferenceBuilder = GradeReferenceBuilder.builder(autoGenerateId);
        this.gradeValueBuilder = GradeValueBuilder.builder();
        this.timestamp = ZonedDateTime.now();
    }

    public static GradeBuilder builder(boolean autoGenerateId) {
        return new GradeBuilder(autoGenerateId);
    }

    public GradeBuilder gradeValue(final GradeValue gradeValue) {
        return numericValue(gradeValue.getNumericValue())
                .textValue(gradeValue.getTextValue())
                .gradingSystem(gradeValue.getGradingSystem());
    }

    public GradeBuilder gradingSystem(String gradingSystem) {
        gradeValueBuilder.gradingSystem(gradingSystem);
        return this;
    }

    public GradeBuilder textValue(String textValue) {
        gradeValueBuilder.textValue(textValue);
        return this;
    }

    public GradeBuilder numericValue(final Number numericValue) {
        gradeValueBuilder.numericValue(numericValue);
        return this;
    }

    public GradeBuilder timestamp(final ZonedDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public GradeBuilder weight(final Number weight) {
        this.weight = new BigDecimal(weight.toString());
        return this;
    }

    public GradeBuilder references(final Map<String, GradeReference> references) {

        this.references = new HashMap<>();
        if (Objects.nonNull(references)) {
            references.keySet().forEach(key -> {
                GradeReference r = references.get(key);
                this.references.put(key, new BasicGradeReference(r.getId(), r.getType(), r.getDescription(), r.getExtensions()));
            });
        }
        return this;
    }

    public GradeBuilder reference(String key, final GradeReference reference) {
        if (Objects.isNull(references)) {
            references = new HashMap<>();
        }

        this.references.put(key, new BasicGradeReference(reference.getId(), reference.getType(),
                reference.getDescription(), reference.getExtensions()));
        return this;
    }

    public GradeBuilder id(String id) {
        gradeReferenceBuilder.id(id);
        return this;
    }

    public GradeBuilder type(String type) {
        gradeReferenceBuilder.type(type);
        return this;
    }

    public GradeBuilder description(String description) {
        gradeReferenceBuilder.description(description);
        return this;
    }

    public GradeBuilder extensions(final Map<String, Object> extensions) {
        gradeReferenceBuilder.extensions(extensions);
        return this;
    }

    public GradeBuilder extension(String key, Object value) {
        gradeReferenceBuilder.extension(key, value);
        return this;
    }

    public Grade build() {
        Grade grade = new BasicGrade(gradeReferenceBuilder.build(), gradeValueBuilder.build(), timestamp, weight, references);
        clear();
        return grade;
    }

    public GradeBuilder clear() {
        gradeReferenceBuilder.clear();
        gradeValueBuilder.clear();
        timestamp = ZonedDateTime.now();
        references = new HashMap<>();
        weight = BigDecimal.ONE;
        return this;
    }
}
