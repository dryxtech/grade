package com.dryxtech.grade.util;

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.model.BasicGrade;
import com.dryxtech.grade.model.BasicGradingSystem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A file utility class for grading system files
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class GradeFileUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }

    private GradeFileUtil() {
        // Utility
    }

    public static GradingSystem loadBundledGradingSystem(final String filename) throws IOException {
        String resourcePath = GradeConstants.GRADING_SYSTEM_FILE_FOLDER + "/" + filename;
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            return mapper.readValue(is, BasicGradingSystem.class);
        }
    }

    public static void saveGradingSystem(final File file, final GradingSystem gradingSystem) throws IOException {
        mapper.writeValue(file, gradingSystem);
    }

    public static void saveGradingSystems(final File file, final Collection<GradingSystem> gradingSystems) throws IOException {
        mapper.writeValue(file, gradingSystems);
    }

    public static List<GradingSystem> loadGradingSystems(final File file) throws IOException {
        return mapper.readValue(file, new TypeReference<List<BasicGradingSystem>>() {
        })
                .stream().map(gradingSystem -> (GradingSystem) gradingSystem).collect(Collectors.toList());
    }

    public static GradingSystem loadGradingSystem(final File file) throws IOException {
        return mapper.readValue(file, BasicGradingSystem.class);
    }

    public static void saveGrade(final File file, final Grade grade) throws IOException {
        mapper.writeValue(file, grade);
    }

    public static void saveGrades(final File file, final Collection<Grade> grades) throws IOException {
        mapper.writeValue(file, grades);
    }

    public static List<Grade> loadGrades(final File file) throws IOException {
        return mapper.readValue(file, new TypeReference<List<BasicGrade>>() {
        })
                .stream().map(grade -> (Grade) grade).collect(Collectors.toList());
    }

    public static Grade loadGrade(final File file) throws IOException {
        return mapper.readValue(file, BasicGrade.class);
    }
}
