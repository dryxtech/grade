package com.dryxtech.grade.control;

import java.util.Arrays;
import java.util.Collection;

/**
 * This class contains constants for managing the grading process.
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class GradeConstants {

    private GradeConstants() {
        // Constants
    }

    // grading system constants
    public static final String DEFAULT_GRADING_SYSTEM_ID = "default";
    public static final String GRADING_SYSTEM_FILE_EXTENSION = ".json";
    public static final String GRADING_SYSTEM_FILE_FOLDER = "grading-systems";

    // default grading system (concrete class implementation)
    public static final String Z_GRADING_SYSTEM = "general.percent.z-system.us";

    // bundled grading systems (under resources/grading-system folder)
    public static final String STANDARD_ACADEMIC_SYSTEM = "academic.percent.standard.us";
    public static final String STANDARD_BINARY_PASS_FAIL_SYSTEM = "general.binary.pass-fail.us";
    public static final String STANDARD_FIVE_STAR_SYSTEM = "general.rating.five-star.us";
    public static final String STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM = "academic.gpa.standard-plus-minus.us";
    public static final String STANDARD_PERCENT_PASS_FAIL_SYSTEM = "general.percent.pass-fail.us";
    public static final String STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM = "academic.percent.standard-plus-minus.us";
    public static final String STANDARD_POOR_TO_EXCELLENT_SYSTEM = "general.percent.poor-to-excellent.us";
    public static final String STANDARD_WIN_DRAW_LOSE_SYSTEM = "general.ternary.win-draw-lose.us";

    // known grading system ids
    static final String[] GRADING_SYSTEM_IDS = {
            STANDARD_ACADEMIC_SYSTEM,
            STANDARD_BINARY_PASS_FAIL_SYSTEM,
            STANDARD_FIVE_STAR_SYSTEM,
            STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM,
            STANDARD_PERCENT_PASS_FAIL_SYSTEM,
            STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM,
            STANDARD_POOR_TO_EXCELLENT_SYSTEM,
            STANDARD_WIN_DRAW_LOSE_SYSTEM,
            Z_GRADING_SYSTEM
    };

    // bundled grading system definition files
    static final String[] GRADING_SYSTEM_DEFINITION_FILES = {
            STANDARD_ACADEMIC_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_BINARY_PASS_FAIL_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_FIVE_STAR_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_PERCENT_PASS_FAIL_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_POOR_TO_EXCELLENT_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_WIN_DRAW_LOSE_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION
    };

    public static Collection<String> getBundledGradingSystemFiles() {
        return Arrays.asList(GRADING_SYSTEM_DEFINITION_FILES);
    }

    public static Collection<String> getGradingSystemIds() {
        return Arrays.asList(GRADING_SYSTEM_IDS);
    }
}
