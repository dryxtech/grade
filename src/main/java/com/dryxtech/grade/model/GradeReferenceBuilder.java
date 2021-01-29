package com.dryxtech.grade.model;

import com.dryxtech.grade.api.GradeReference;
import com.dryxtech.grade.util.IdentifierUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A builder class for basic grade references
 *
 * @author Drew Griffin
 * @since 1.0
 */
public class GradeReferenceBuilder {

    private boolean autoGenerateId = false;
    private String id;
    private String type;
    private String description;
    private Map<String, Object> extensions;

    public GradeReferenceBuilder(boolean autoGenerateId) {
        this.autoGenerateId = autoGenerateId;
    }

    public GradeReferenceBuilder(GradeReference gradeReference) {
        this.id = gradeReference.getId();
        this.type = gradeReference.getType();
        this.description = gradeReference.getDescription();
        if (Objects.nonNull(gradeReference.getExtensions())) {
            this.extensions = new HashMap<>(gradeReference.getExtensions());
        }
    }

    public GradeReferenceBuilder(String id, String type, String description, final Map<String, Object> extensions) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.extensions = extensions;
    }

    public static GradeReferenceBuilder builder() {
        return new GradeReferenceBuilder(false);
    }

    public static GradeReferenceBuilder builder(boolean autoGenerateId) {
        return new GradeReferenceBuilder(autoGenerateId);
    }

    public static GradeReferenceBuilder builder(GradeReference gradeReference) {
        return new GradeReferenceBuilder(gradeReference);
    }

    public GradeReferenceBuilder id(String id) {
        this.id = id;
        return this;
    }

    public GradeReferenceBuilder type(String type) {
        this.type = type;
        return this;
    }

    public GradeReferenceBuilder description(String description) {
        this.description = description;
        return this;
    }

    public GradeReferenceBuilder extensions(Map<String, Object> extensions) {
        if (Objects.nonNull(extensions)) {
            this.extensions = new HashMap<>(extensions);
        } else {
            this.extensions = null;
        }
        return this;
    }

    public GradeReferenceBuilder extension(String key, Object value) {
        if (Objects.isNull(extensions)) {
            extensions = new HashMap<>();
        }

        this.extensions.put(key, value);
        return this;
    }

    public GradeReference build() {
        if (autoGenerateId) {
            id = IdentifierUtil.generateIdString();
        }
        GradeReference reference = new BasicGradeReference(id, type, description, extensions);
        clear();
        return reference;
    }

    public GradeReferenceBuilder clear() {
        id = null;
        type = null;
        description = null;
        extensions = null;
        return this;
    }
}
