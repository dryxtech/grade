package com.dryxtech.grade.util;

import com.dryxtech.grade.GradeTesting;
import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.model.GradeRank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GradeMathUtilTest {

    @Test
    void calculateGradeDistribution() {

        Map<String, Integer> result = GradeMathUtil.calculateGradeDistribution(Arrays.asList(
                GradeTesting.mockGradeValue(75, "B"),
                GradeTesting.mockGradeValue(100, "A"),
                GradeTesting.mockGradeValue(40, "C"),
                GradeTesting.mockGradeValue(95, "A"),
                GradeTesting.mockGradeValue(78, "B"),
                GradeTesting.mockGradeValue(98, "A")
        ));

        assertEquals("{A=3, B=2, C=1}", result.toString());
    }

    @Test
    void calculateGradeAverageWithDefault() {

        BigDecimal result = GradeMathUtil.calculateGradeAverage(null, new BigDecimal(100));
        assertEquals(new BigDecimal(100), result);

        result = GradeMathUtil.calculateGradeAverage(Collections.emptyList(), new BigDecimal(101));
        assertEquals(new BigDecimal(101), result);
    }

    @Test
    void calculateGradeAverageWithPositiveValues() {

        List<Grade> grades = GradeTesting.mockGrades(1, 10);
        Optional<BigDecimal> result = GradeMathUtil.calculateGradeAverage(grades);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("5.5"), result.get());
    }

    @Test
    void calculateGradeAverageWithMixedValues() {

        List<Grade> grades = GradeTesting.mockGrades(-10, 21);
        Optional<BigDecimal> result = GradeMathUtil.calculateGradeAverage(grades);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("0"), result.get());
    }

    @Test
    void calculateAverage() {

        List<Number> values = IntStream.range(1, 100).mapToObj(BigDecimal::new).collect(Collectors.toList());

        Optional<BigDecimal> result = GradeMathUtil.calculateAverage(values);

        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("50"), result.get());
    }

    @Test
    void calculateAverageWithDefault() {

        BigDecimal result = GradeMathUtil.calculateAverage(null, new BigDecimal(100));
        assertEquals(new BigDecimal(100), result);

        result = GradeMathUtil.calculateAverage(Collections.emptyList(), new BigDecimal(101));
        assertEquals(new BigDecimal(101), result);
    }

    @Test
    void calculateWeightedAverage() {

        List<Grade> inputGrades = Arrays.asList(
                GradeTesting.mockGrade(GradeTesting.mockGradeReference("homework", ""), GradeTesting.mockGradeValue(95, "A"), .60),
                GradeTesting.mockGrade(GradeTesting.mockGradeReference("final-exam", ""), GradeTesting.mockGradeValue(85, "B"), .30),
                GradeTesting.mockGrade(GradeTesting.mockGradeReference("participation", ""), GradeTesting.mockGradeValue(75, "C"), .10)
        );

        Optional<BigDecimal> weightedAverage = GradeMathUtil.calculateWeightedAverage(inputGrades);

        assertTrue(weightedAverage.isPresent());
        assertEquals(new BigDecimal(90), weightedAverage.get());
    }

    @Test
    void calculateWeightedAverageWithDefault() {

        BigDecimal result = GradeMathUtil.calculateWeightedAverage(null, new BigDecimal(100));
        assertEquals(new BigDecimal(100), result);

        result = GradeMathUtil.calculateWeightedAverage(Collections.emptyList(), new BigDecimal(101));
        assertEquals(new BigDecimal(101), result);
    }

    @Test
    void rankGradesHiToLow() {

        List<Grade> grades = GradeTesting.mockGrades(80, 20);
        grades.addAll(GradeTesting.mockGrades(90, 10));
        List<GradeRank> gradeRanks = GradeMathUtil.rankGradesHiToLow(grades);

        AtomicLong lastRank = new AtomicLong(0);
        AtomicReference<BigDecimal> lastValue = new AtomicReference<>(new BigDecimal(100));
        gradeRanks.forEach(gradeRank -> {
            assertTrue(gradeRank.getRank() >= lastRank.get());
            assertTrue(gradeRank.getGrade().getNumericValue().compareTo(lastValue.get()) <= 0);
            lastRank.set(gradeRank.getRank());
            lastValue.set(gradeRank.getGrade().getNumericValue());
        });
    }

    @Test
    void rankGradesLowToHigh() {

        List<Grade> grades = GradeTesting.mockGrades(20, 40);
        grades.addAll(GradeTesting.mockGrades(30, 50));
        List<GradeRank> gradeRanks = GradeMathUtil.rankGradesLowToHigh(grades);

        AtomicLong lastRank = new AtomicLong(0);
        AtomicReference<BigDecimal> lastValue = new AtomicReference<>(new BigDecimal(0));
        gradeRanks.forEach(gradeRank -> {
            assertTrue(gradeRank.getRank() >= lastRank.get());
            assertTrue(gradeRank.getGrade().getNumericValue().compareTo(lastValue.get()) >= 0);
            lastRank.set(gradeRank.getRank());
            lastValue.set(gradeRank.getGrade().getNumericValue());
        });
    }

    @Test
    void rankGradesNullComparator() {

        List<Grade> grades = GradeTesting.mockGrades(10, 1, 9, 2, 5);
        List<GradeRank> gradeRanks = GradeMathUtil.rankGrades(grades, null);

        assertEquals(5, gradeRanks.size());
        IntStream.range(0, 5).forEach(n -> {
            assertEquals(n + 1, gradeRanks.get(n).getRank());
            Assertions.assertEquals(grades.get(n).getNumericValue(), gradeRanks.get(n).getGrade().getNumericValue());
        });
    }
}
