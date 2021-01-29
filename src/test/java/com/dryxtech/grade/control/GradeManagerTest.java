package com.dryxtech.grade.control;

import com.dryxtech.grade.GradeTesting;
import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.Grader;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.model.GradeRank;
import com.dryxtech.grade.system.converter.TextValueBasedConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GradeManagerTest {

    private static final String ORGANIZATION = "DRYXTECH";
    private static final String OWNER = "football-team";
    private GradeManager manager;

    @BeforeEach
    void setUp() {
        Map<String, Object> managementInfo = new HashMap<>();
        managementInfo.put("organization", ORGANIZATION);
        managementInfo.put("owner", OWNER);
        this.manager = new GradeManager(managementInfo);
    }

    @Test
    void getManagementInformation() {
        Map<String, Object> managementInfo = manager.getManagementInformation();
        assertEquals(managementInfo.get("organization"), ORGANIZATION);
        assertEquals(managementInfo.get("owner"), OWNER);
    }

    @Test
    void loadBundledGradingSystems() {
        int beforeCount = manager.getGradingSystems().size();
        manager.loadBundledGradingSystems();
        int afterCount = manager.getGradingSystems().size();

        assertTrue(afterCount > beforeCount);
    }

    @Test
    void registerAndLookupGradingSystem() {
        GradingSystem gradingSystem = GradeTesting.mockGradingSystem();
        manager.registerGradingSystem("test", gradingSystem);
        GradingSystem result = Assertions.assertDoesNotThrow(() -> manager.lookupGradingSystem("test"));
        assertEquals(gradingSystem, result);
    }

    @Test
    void setDefaultGradingSystem() {
        GradingSystem gradingSystem = GradeTesting.mockGradingSystem();
        manager.setDefaultGradingSystem(gradingSystem);
        GradeValue gradeValue = Assertions.assertDoesNotThrow(() -> manager.grade(1));
        assertEquals(gradingSystem.getTextValue(BigDecimal.ONE).orElse("NOT_T"), gradeValue.getTextValue());
    }

    @Test
    void getGradingSystemRegistry() {
        assertNotNull(manager.getGradingSystemRegistry());
    }

    @Test
    void getGradingSystems() {
        assertNotNull(manager.getGradingSystems());
    }

    @Test
    void getGrader() {
        Grader<Number> grader = Assertions.assertDoesNotThrow(() -> manager.getGrader());
        assertNotNull(grader);
    }

    @Test
    void getGraderForGradingSystem() {
        Grader<Number> grader = Assertions.assertDoesNotThrow(() -> manager.getGrader(GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(grader);
    }

    @Test
    void getAverageGrader() {
        Grader<Collection<Number>> grader = Assertions.assertDoesNotThrow(() -> manager.getAverageGrader());
        assertNotNull(grader);
    }

    @Test
    void getAverageGraderForGradingSystem() {
        Grader<Collection<Number>> grader = Assertions.assertDoesNotThrow(() -> manager.getAverageGrader(GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(grader);
    }

    @Test
    void getRollupGrader() {
        Grader<Collection<GradeValue>> grader = Assertions.assertDoesNotThrow(() -> manager.getRollupGrader());
        assertNotNull(grader);
    }

    @Test
    void getRollupGraderForGradingSystem() {
        Grader<Collection<GradeValue>> grader = Assertions.assertDoesNotThrow(() -> manager.getRollupGrader(GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(grader);
    }

    @Test
    void getWeightedRollupGrader() {
        Grader<Collection<Grade>> grader = Assertions.assertDoesNotThrow(() -> manager.getWeightedRollupGrader());
        assertNotNull(grader);
    }

    @Test
    void testGetWeightedRollupGrader() {
        Grader<Collection<Grade>> grader = Assertions.assertDoesNotThrow(() -> manager.getWeightedRollupGrader(GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(grader);
    }

    @Test
    void grade() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.grade(100));
        assertNotNull(result);
    }

    @Test
    void gradeAverage() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.gradeAverage(Collections.singleton(100)));
        assertNotNull(result);
    }

    @Test
    void gradeRollup() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.gradeRollup(
                Collections.singleton(GradeTesting.mockGradeValue(100, "V"))));
        assertNotNull(result);
    }

    @Test
    void gradeWeightedRollup() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.gradeWeightedRollup(GradeTesting.mockGrades(1, 10)));
        assertNotNull(result);
    }

    @Test
    void gradeForGradingSystem() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.grade(100, GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(result);
    }

    @Test
    void gradeAverageForGradingSystem() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.gradeAverage(Collections.singleton(100), GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(result);
    }

    @Test
    void gradeRollupForGradingSystem() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.gradeRollup(
                Collections.singleton(GradeTesting.mockGradeValue(100, "V")), GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(result);
    }

    @Test
    void gradeWeightedRollupForGradingSystem() {
        GradeValue result = Assertions.assertDoesNotThrow(() -> manager.gradeWeightedRollup(
                GradeTesting.mockGrades(1, 10), GradeConstants.Z_GRADING_SYSTEM));
        assertNotNull(result);
    }

    @Test
    void rankHiToLow() {
        List<Grade> grades = GradeTesting.mockGrades(1, 10);
        List<GradeRank> result = new ArrayList<>(manager.rankHiToLow(grades));
        assertEquals(result.get(0).getGrade().getNumericValue().intValue(), 10);
    }

    @Test
    void rankLowToHigh() {
        List<Grade> grades = GradeTesting.mockGrades(1, 10);
        List<GradeRank> result = new ArrayList<>(manager.rankLowToHigh(grades));
        assertEquals(result.get(0).getGrade().getNumericValue().intValue(), 1);
    }

    @Test
    void calculateDistribution() {
        List<Grade> grades = GradeTesting.mockGrades(1, 10);
        Map<String, Integer> result = manager.calculateDistribution(grades);
        assertEquals(2, result.size());
    }

    @Test
    void getGradeValueBuilder() {
        assertNotNull(manager.getGradeValueBuilder());
    }

    @Test
    void getGradeReferenceBuilder() {
        assertNotNull(manager.getGradeReferenceBuilder(true));
    }

    @Test
    void getGradeBuilder() {
        assertNotNull(manager.getGradeBuilder(true));
    }

    @Test
    void getGradingSystemBuilder() {
        assertNotNull(manager.getGradingSystemBuilder());
    }

    @Test
    void getGradeValueRangeBuilder() {
        assertNotNull(manager.getGradeValueRangeBuilder());
    }

    @Test
    void recordAndFind() {
        manager.record(GradeTesting.mockGrades(1).get(0));
        assertEquals(1, manager.findAll().size());
    }

    @Test
    void getGradeBook() {
        assertNotNull(manager.getGradeBook());
    }

    @Test
    void recordAndFindMultiple() {
        manager.record(GradeTesting.mockGrades(1, 10));
        assertEquals(10, manager.findAll().size());

        Collection<Grade> result = manager.find(g -> (g.getNumericValue().intValue() <= 5));
        assertEquals(5, result.size());
    }

    @Test
    void erase() {
        List<Grade> grades = GradeTesting.mockGrades(1, 10);
        manager.record(grades);
        assertTrue(manager.erase(grades.get(0)));
        assertEquals(9, manager.findAll().size());
    }

    @Test
    void eraseForSearch() {
        List<Grade> grades = GradeTesting.mockGrades(1, 10);
        manager.record(grades);
        manager.erase(g -> (g.getNumericValue().intValue() > 5));
        assertEquals(5, manager.findAll().size());
    }

    @Test
    void registerAndLookupConverter() {
        GradeConverter converter = new TextValueBasedConverter(manager.getGradingSystemRegistry());
        manager.registerConverter(GradeConstants.Z_GRADING_SYSTEM, "some-other-grading-system", converter);
        GradeConverter result = Assertions.assertDoesNotThrow(() -> manager.lookupConverter(GradeConstants.Z_GRADING_SYSTEM, "some-other-grading-system"));
        assertEquals(converter, result);
    }
}
