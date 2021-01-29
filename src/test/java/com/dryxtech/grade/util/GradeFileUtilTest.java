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

package com.dryxtech.grade.util;

import com.dryxtech.grade.GradeTesting;
import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.model.BasicGrade;
import com.dryxtech.grade.system.ZGradingSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class GradeFileUtilTest {

    @TempDir
    static Path sharedTempDir;

    private GradingSystem testGradingSystem;
    private Grade testGrade;

    @BeforeEach
    void setUp() {
        this.testGradingSystem = new ZGradingSystem();
        this.testGrade = new BasicGrade(
                GradeTesting.GRADE_ID,
                GradeTesting.GRADE_TYPE,
                GradeTesting.GRADE_DESCRIPTION,
                GradeTesting.GRADE_EXTENSIONS,
                GradeTesting.GRADE_NUMERIC_VALUE,
                GradeTesting.GRADE_TEXT_VALUE,
                GradeTesting.GRADE_GRADING_SYSTEM,
                GradeTesting.GRADE_TIMESTAMP,
                GradeTesting.GRADE_WEIGHT,
                GradeTesting.GRADE_REFERENCES
        );
    }

    @Test
    void testLoadBundledGradingSystem() {

        GradeConstants.getBundledGradingSystemFiles().forEach(filename -> {
                    try {
                        GradingSystem gradingSystem = GradeFileUtil.loadBundledGradingSystem(filename);
                        assertNotNull(gradingSystem);
                    } catch (IOException e) {
                        fail("failed to load bundle from file " + filename, e);
                    }
                }
        );
    }

    @Test
    void testGradingSystem() throws IOException {
        Path path = sharedTempDir.resolve("test-grading-system.json");

        GradeFileUtil.saveGradingSystem(path.toFile(), testGradingSystem);
        assertTrue(path.toFile().length() > 0);

        GradingSystem gradingSystem = GradeFileUtil.loadGradingSystem(path.toFile());
        assertEquals(testGradingSystem.getId(), gradingSystem.getId());
    }

    @Test
    void testGradingSystems() throws IOException {
        Path path = sharedTempDir.resolve("test-grading-systems.json");

        GradeFileUtil.saveGradingSystems(path.toFile(), Arrays.asList(testGradingSystem, testGradingSystem));
        assertTrue(path.toFile().length() > 0);

        List<GradingSystem> gradingSystems = GradeFileUtil.loadGradingSystems(path.toFile());
        assertEquals(2, gradingSystems.size());
        assertEquals(gradingSystems.get(0).getId(), testGradingSystem.getId());
        assertEquals(gradingSystems.get(1).getId(), testGradingSystem.getId());
    }

    @Test
    void testGrade() throws IOException {
        Path path = sharedTempDir.resolve("test-grade.json");

        GradeFileUtil.saveGrade(path.toFile(), testGrade);
        assertTrue(path.toFile().length() > 0);

        Grade grade = GradeFileUtil.loadGrade(path.toFile());
        assertEquals(testGrade, grade);
    }

    @Test
    void testGrades() throws IOException {
        Path path = sharedTempDir.resolve("test-grades.json");

        GradeFileUtil.saveGrades(path.toFile(), Arrays.asList(testGrade, testGrade));
        assertTrue(path.toFile().length() > 0);

        List<Grade> grades = GradeFileUtil.loadGrades(path.toFile());
        assertEquals(2, grades.size());
        assertEquals(grades.get(0), testGrade);
        assertEquals(grades.get(1), testGrade);
    }
}
