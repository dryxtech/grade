/*
 * Copyright (c) 2021 DRYXTECH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    // grading system constants
    public static final String DEFAULT_GRADING_SYSTEM_ID = "default";
    public static final String GRADING_SYSTEM_FILE_EXTENSION = ".json";
    public static final String GRADING_SYSTEM_FILE_FOLDER = "grading-systems";
    // default grading system (concrete class implementation)
    public static final String Z_GRADING_SYSTEM = "general.percent.z-system.us";
    // bundled grading systems (under resources/grading-system folder)
    public static final String STANDARD_ACADEMIC_FRANCE_SYSTEM = "academic.percent.standard.fr";
    public static final String STANDARD_ACADEMIC_JAPAN_SYSTEM = "academic.percent.standard.jp";
    public static final String STANDARD_ACADEMIC_PERU_SYSTEM = "academic.percent.standard.pe";
    public static final String STANDARD_ACADEMIC_RUSSIA_SYSTEM = "academic.percent.standard.ru";
    public static final String STANDARD_ACADEMIC_SYSTEM = "academic.percent.standard.us";
    public static final String STANDARD_BINARY_PASS_FAIL_SYSTEM = "general.binary.pass-fail.us";
    public static final String STANDARD_CHESS_ELO_SYSTEM = "game.rating.chess.elo";
    public static final String STANDARD_CHESS_USCF_SYSTEM = "game.rating.chess.uscf";
    public static final String STANDARD_FIVE_STAR_SYSTEM = "general.rating.five-star.us";
    public static final String STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM = "academic.gpa.standard-plus-minus.us";
    public static final String STANDARD_PERCENT_PASS_FAIL_SYSTEM = "general.percent.pass-fail.us";
    public static final String STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM = "academic.percent.standard-plus-minus.us";
    public static final String STANDARD_POOR_TO_EXCELLENT_SYSTEM = "general.percent.poor-to-excellent.us";
    public static final String STANDARD_RESTAURANT_INSPECTION_SYSTEM = "restaurant.rating.inspection.us";
    public static final String STANDARD_RESTAURANT_RATING_SYSTEM = "restaurant.rating.michelin.us";
    public static final String STANDARD_SEVEN_STAR_SYSTEM = "general.rating.seven-star.us";
    public static final String STANDARD_THREE_STAR_SYSTEM = "general.rating.three-star.us";
    public static final String STANDARD_WIN_DRAW_LOSE_SYSTEM = "general.ternary.win-draw-lose.us";

    // known grading system ids
    static final String[] GRADING_SYSTEM_IDS = {
            STANDARD_ACADEMIC_FRANCE_SYSTEM,
            STANDARD_ACADEMIC_JAPAN_SYSTEM,
            STANDARD_ACADEMIC_PERU_SYSTEM,
            STANDARD_ACADEMIC_RUSSIA_SYSTEM,
            STANDARD_ACADEMIC_SYSTEM,
            STANDARD_BINARY_PASS_FAIL_SYSTEM,
            STANDARD_CHESS_ELO_SYSTEM,
            STANDARD_CHESS_USCF_SYSTEM,
            STANDARD_FIVE_STAR_SYSTEM,
            STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM,
            STANDARD_PERCENT_PASS_FAIL_SYSTEM,
            STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM,
            STANDARD_POOR_TO_EXCELLENT_SYSTEM,
            STANDARD_RESTAURANT_INSPECTION_SYSTEM,
            STANDARD_RESTAURANT_RATING_SYSTEM,
            STANDARD_SEVEN_STAR_SYSTEM,
            STANDARD_THREE_STAR_SYSTEM,
            STANDARD_WIN_DRAW_LOSE_SYSTEM,
            Z_GRADING_SYSTEM
    };
    
    // bundled grading system definition files
    static final String[] GRADING_SYSTEM_DEFINITION_FILES = {
            STANDARD_ACADEMIC_FRANCE_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_ACADEMIC_JAPAN_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_ACADEMIC_PERU_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_ACADEMIC_RUSSIA_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_ACADEMIC_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_BINARY_PASS_FAIL_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_CHESS_ELO_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_CHESS_USCF_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_FIVE_STAR_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_PERCENT_PASS_FAIL_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_POOR_TO_EXCELLENT_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_RESTAURANT_INSPECTION_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_RESTAURANT_RATING_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_SEVEN_STAR_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_THREE_STAR_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            STANDARD_WIN_DRAW_LOSE_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION,
            Z_GRADING_SYSTEM + GRADING_SYSTEM_FILE_EXTENSION
    };

    private GradeConstants() {
        // Constants
    }

    public static Collection<String> getBundledGradingSystemFiles() {
        return Arrays.asList(GRADING_SYSTEM_DEFINITION_FILES);
    }

    public static Collection<String> getGradingSystemIds() {
        return Arrays.asList(GRADING_SYSTEM_IDS);
    }
}
