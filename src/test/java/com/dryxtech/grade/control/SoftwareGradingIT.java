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

import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.api.Question;
import com.dryxtech.grade.api.TestException;
import com.dryxtech.grade.api.TestScore;
import com.dryxtech.grade.model.BasicQuestion;
import com.dryxtech.grade.model.BasicQuestionValue;
import com.dryxtech.grade.model.BasicTest;
import com.dryxtech.grade.model.BasicTestSubmission;
import com.dryxtech.grade.model.GradeBuilder;
import com.dryxtech.grade.model.GradeReferenceBuilder;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.system.converter.BinaryToPercentConverter;
import com.dryxtech.grade.util.GradeFileUtil;
import com.dryxtech.grade.util.IdentifierUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SoftwareGradingIT {

    @TempDir
    static Path sharedTempDir;

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareGradingIT.class);
    private GradeManager gradeManager;
    private TestManager testManager;

    @Test
    public void testSoftwareGrading() {
        try {
            setupManager();
            gradeYourSoftware();
            persistYourSoftwareGrade();

        } catch (GradeException | TestException | IOException ex) {
            fail("standard academic grading scenario test failed!", ex);
        }
    }

    private void setupManager() throws GradingSystemNotFoundException, IOException {

        Map<String, Object> managementInfo = new HashMap<>();
        managementInfo.put("organization", "BestSoftwareCompanyEver, Inc");
        managementInfo.put("division", "code-eagles");

        LOGGER.info("setup grade manager");
        gradeManager = new GradeManager(
                new GradingSystemRegistry(),
                new SimpleMemoryGradeBook<>(),
                managementInfo
        );

        assertEquals(managementInfo, gradeManager.getManagementInformation());

        // register standard-poor-to-excellent grading system as default
        GradingSystem gradingSystem = GradeFileUtil.loadBundledGradingSystem(
                GradeConstants.STANDARD_POOR_TO_EXCELLENT_SYSTEM + GradeConstants.GRADING_SYSTEM_FILE_EXTENSION);
        gradeManager.registerGradingSystem(gradingSystem.getId(), gradingSystem);
        gradeManager.registerGradingSystem(gradingSystem.getId(), gradingSystem);
        gradeManager.setDefaultGradingSystem(gradingSystem);

        assertEquals(gradingSystem, gradeManager.lookupGradingSystem(gradingSystem.getId()));

        // register pass fail system
        gradingSystem = GradeFileUtil.loadBundledGradingSystem(
                GradeConstants.STANDARD_BINARY_PASS_FAIL_SYSTEM + GradeConstants.GRADING_SYSTEM_FILE_EXTENSION);
        gradeManager.registerGradingSystem(gradingSystem.getId(), gradingSystem);

        // register converters
        GradeConverter binaryToPercentConverter = new BinaryToPercentConverter(gradeManager.getGradingSystemRegistry());
        gradeManager.registerConverter(GradeConstants.STANDARD_BINARY_PASS_FAIL_SYSTEM,
                GradeConstants.STANDARD_POOR_TO_EXCELLENT_SYSTEM, binaryToPercentConverter);

        testManager = new TestManager(managementInfo);
        testManager.record(BasicTest.builder()
                .id(IdentifierUtil.generateIdString())
                .name("software-build-test")
                .questions(Collections.singletonList(BasicQuestion.builder()
                        .id(IdentifierUtil.generateIdString())
                        .text("Did the build pass?")
                        .values(Collections.singletonList(BasicQuestionValue.builder()
                                .id(IdentifierUtil.generateIdString())
                                .value("true")
                                .build()))
                        .build()))
                .build());
    }

    private void gradeYourSoftware() throws GradeException, TestException {

        com.dryxtech.grade.api.Test test = testManager.find(t -> t.getName().equals("software-build-test")).stream().findFirst().get();
        Map<Question, String> answers = Collections.singletonMap(test.getQuestions().get(0), "true");
        TestScore score = testManager.score(test, BasicTestSubmission.builder()
                .id(IdentifierUtil.generateIdString()).submittedAnswers(answers).build());
        assertEquals(BigDecimal.ONE, score.getValue());

        GradeValue codeBuilds = gradeManager.grade(score.getValue(), GradeConstants.STANDARD_BINARY_PASS_FAIL_SYSTEM);
        GradeValue codeCoverage = gradeManager.grade(81);
        GradeValue codeSmell = gradeManager.grade(70);

        // handles auto-conversion of codeBuilds value to default grading system
        GradeValue average = gradeManager.gradeRollup(Arrays.asList(codeBuilds, codeCoverage, codeSmell));

        assertEquals(84, average.getNumericValue().setScale(0, RoundingMode.CEILING).intValue());

        gradeManager.record(GradeBuilder.builder(true)
                .gradeValue(average)
                .type("software-quality")
                .description("your-application")
                .extension("codeBuilds", codeBuilds)
                .extension("codeCoverage", codeCoverage)
                .extension("codeSmell", codeSmell)
                .reference("application", GradeReferenceBuilder.builder().id("123").type("application")
                        .description("your-application").build())
                .build());
        assertEquals(1, gradeManager.findAll().size());
    }

    private void persistYourSoftwareGrade() throws IOException {

        Path path = sharedTempDir.resolve("your-grades.json");

        GradeFileUtil.saveGrades(path.toFile(), gradeManager.find(grade ->
                    grade.getType().equals("software-quality") && grade.getReference("application").isPresent()));
        assertTrue(path.toFile().exists());

        gradeManager.eraseAll();
        assertEquals(0, gradeManager.findAll().size());
    }
}
