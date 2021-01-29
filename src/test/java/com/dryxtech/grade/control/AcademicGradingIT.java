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

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeException;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.model.GradeBuilder;
import com.dryxtech.grade.system.GradingSystemNotFoundException;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.system.converter.TextValueBasedConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AcademicGradingIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcademicGradingIT.class);
    private GradeManager manager;

    @Test
    public void testAcademicGrading() {
        try {
            setupManager();
            determineClassGrades();
            rankGrades();
            determineFinalGPA();
            clearGradeBook();
        } catch (GradeException ex) {
            fail("standard academic grading scenario test failed!", ex);
        }
    }

    private void setupManager() throws GradingSystemNotFoundException {

        LOGGER.info("setup grade manager");
        manager = new GradeManager(
                new GradingSystemRegistry(),
                new SimpleMemoryGradeBook<>(),
                Collections.singletonMap("organization", "DRYXTECH")
        );
        assertEquals(2, manager.getGradingSystems().size());
        assertTrue(manager.loadBundledGradingSystems());
        assertTrue(manager.getGradingSystems().size() > 2);
        manager.setDefaultGradingSystem(manager.lookupGradingSystem(GradeConstants.STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM));
    }

    private void determineClassGrades() throws GradeException {

        LOGGER.info("grade for class 1 and 2; then get average grade value");

        GradeValue class1GradeValue = manager.grade(92.5);
        GradeValue class2GradeValue = manager.grade(75);
        GradeValue averageGradeValue = manager.gradeAverage(
                Arrays.asList(class1GradeValue.getNumericValue(), class2GradeValue.getNumericValue()));

        assertEquals("A-", class1GradeValue.getTextValue());
        assertEquals("C", class2GradeValue.getTextValue());
        assertEquals("B", averageGradeValue.getTextValue());
        assertEquals(83.75, averageGradeValue.getNumericValue().doubleValue());

        LOGGER.info("then grade for class 3 using weights: final_exam=70%, homework=60%, participation=10%");
        GradeBuilder gradeBuilder = manager.getGradeBuilder(true);
        GradeValue class3GradeValue = manager.gradeWeightedRollup(Arrays.asList(
                gradeBuilder.type("class_final_exam").gradeValue(manager.grade(80)).weight(.30).extension("class", "BIO-111").build(),
                gradeBuilder.type("class_homework").gradeValue(manager.grade(100)).weight(.60).extension("class", "BIO-111").build(),
                gradeBuilder.type("class_participation").gradeValue(manager.grade(50)).weight(.10).extension("class", "BIO-111").build()
        ));

        assertEquals(89, class3GradeValue.getNumericValue().doubleValue());
        assertEquals("B+", class3GradeValue.getTextValue());

        // cache final class grades for later use
        manager.record(gradeBuilder.type("class_final").gradeValue(class1GradeValue).weight(3).extension("class", "ENG-101").build());
        manager.record(gradeBuilder.type("class_final").gradeValue(class2GradeValue).weight(2).extension("class", "PSY-101").build());
        manager.record(gradeBuilder.type("class_final").gradeValue(class3GradeValue).weight(1).extension("class", "BIO-111").build());
    }

    private void rankGrades() throws GradeException {

        LOGGER.info("rank grades from highest to lowest");
        Collection<Grade> grades = manager.find(g -> g.getType().equals("class_final"));

        BigDecimal lastGrade = new BigDecimal(100);
        AtomicLong counter = new AtomicLong();
        manager.rankHiToLow(grades).forEach(gradeRank -> {
            assertTrue(gradeRank.getGrade().getNumericValue().compareTo(lastGrade) <= 0);
            Assertions.assertEquals(counter.incrementAndGet(), gradeRank.getRank());
        });
    }

    private void determineFinalGPA() throws GradeException {

        LOGGER.info("convert final percent grades for all 3 classes to a single gpa");
        Collection<Grade> grades = manager.find(g -> g.getType().equals("class_final"));

        GradeConverter converter = new TextValueBasedConverter(manager.getGradingSystemRegistry());
        List<Grade> convertedGrades = grades.stream().map(g -> {
            try {
                return new GradeBuilder(g).gradeValue(converter.convert(g, GradeConstants.STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM)).build();
            } catch (GradeException e) {
                throw new RuntimeException("failed to convert grade", e);
            }
        }).collect(Collectors.toList());

        GradeValue gpaGradeValue = manager.gradeWeightedRollup(convertedGrades, GradeConstants.STANDARD_GPA_PLUS_MINUS_ACADEMIC_SYSTEM);
        assertEquals(3.16, gpaGradeValue.getNumericValue().setScale(2, RoundingMode.FLOOR).doubleValue());
        assertEquals("B", gpaGradeValue.getTextValue());

        manager.record(manager.getGradeBuilder(true).type("semester_gpa_final").gradeValue(gpaGradeValue)
                .extension("semester", "2").build());
    }

    private void clearGradeBook() throws GradeException {

        LOGGER.info("cleanup grade manager");

        LOGGER.info("current grade book grades");
        manager.findAll().forEach(grade -> LOGGER.info(grade.toString()));

        long numErased = manager.erase(Objects::nonNull).size();
        assertEquals(4, numErased);
        assertEquals(0, manager.findAll().size());
    }
}
