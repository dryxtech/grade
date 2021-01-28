package com.dryxtech.grade.util;

import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.model.GradeRank;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

/**
 * A math utility class for grading
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class GradeMathUtil {

    private GradeMathUtil() {
        // Utility Class
    }

    public static BigDecimal calculateGradeAverage(final Collection<? extends GradeValue> values, BigDecimal defaultAverage) {
        return calculateGradeAverage(values).orElse(defaultAverage);
    }

    public static Optional<BigDecimal> calculateGradeAverage(final Collection<? extends GradeValue> values) {

        if (Objects.isNull(values) || values.isEmpty()) {
            return Optional.empty();
        } else {
            return calculateAverage(values.stream()
                    .map(GradeValue::getNumericValue)
                    .collect(Collectors.toList()));
        }
    }

    public static Optional<BigDecimal> calculateAverage(final Collection<? extends Number> values) {

        if (Objects.isNull(values) || values.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal sum = values.stream()
                .filter(Objects::nonNull)
                .map(v -> new BigDecimal(v.toString()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sum.equals(BigDecimal.ZERO)) {
            return Optional.of(sum);
        } else {
            return Optional.of(sum.divide(new BigDecimal(values.size()), MathContext.DECIMAL128));
        }
    }

    public static BigDecimal calculateAverage(final Collection<? extends Number> values, BigDecimal defaultAverage) {
        return calculateAverage(values).orElse(defaultAverage);
    }

    public static BigDecimal calculateWeightedAverage(final Collection<? extends Grade> grades, BigDecimal defaultAverage) {
        return calculateWeightedAverage(grades).orElse(defaultAverage);
    }

    public static Optional<BigDecimal> calculateWeightedAverage(final Collection<? extends Grade> grades) {

        if (Objects.isNull(grades) || grades.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal sumOfWeights = grades.stream()
                .filter(Objects::nonNull)
                .map(Grade::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumOfGradePoints = grades.stream()
                .filter(Objects::nonNull)
                .map(grade -> grade.getWeight().multiply(grade.getNumericValue(), MathContext.DECIMAL128))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (sumOfWeights.equals(BigDecimal.ZERO) || sumOfGradePoints.equals(BigDecimal.ZERO)) {
            return Optional.of(BigDecimal.ZERO);
        } else {
            return Optional.of(sumOfGradePoints.divide(sumOfWeights, MathContext.DECIMAL128));
        }
    }

    /**
     * Create a map of the distribution of grades per each grade text value
     * note: order of the result map values is hi-to-low based on grade numeric value
     *
     * @param grades to analyze
     * @return map of distribution of grades per grade text value
     */
    public static Map<String, Integer> calculateGradeDistribution(final Collection<? extends GradeValue> grades) {

        Map<String, Integer> distribution = new LinkedHashMap<>();
        if (Objects.nonNull(grades) && !grades.isEmpty()) {

            List<GradeValue> gradeList = new ArrayList<>(grades);
            gradeList.sort(comparing(GradeValue::getNumericValue, reverseOrder()));

            gradeList.forEach(value -> {
                Integer currentValue = distribution.getOrDefault(value.getTextValue(), 0);
                distribution.put(value.getTextValue(), currentValue + 1);
            });
        }

        return distribution;
    }

    public static List<GradeRank> rankGradesHiToLow(final Collection<? extends Grade> grades) {
        return rankGrades(grades, comparing(Grade::getNumericValue, reverseOrder()).thenComparing(Grade::getTimestamp));
    }

    public static List<GradeRank> rankGrades(final Collection<? extends Grade> grades, Comparator<Grade> comparator) {

        if (Objects.isNull(grades) || grades.isEmpty()) {
            return Collections.emptyList();
        }

        List<GradeRank> rankedGrades = new ArrayList<>();
        AtomicLong rank = new AtomicLong(1);
        List<Grade> gradeList = new ArrayList<>(grades);
        if (Objects.nonNull(comparator)) {
            gradeList.sort(comparator);
        }

        gradeList.forEach(grade -> {
            if ((!rankedGrades.isEmpty()) &&
                    (grade.getNumericValue().compareTo(rankedGrades.get(rankedGrades.size() - 1).getGrade().getNumericValue()) != 0)) {
                rankedGrades.add(new GradeRank(grade, rank.incrementAndGet()));
            } else {
                rankedGrades.add(new GradeRank(grade, rank.get()));
            }
        });

        return rankedGrades;
    }

    public static List<GradeRank> rankGradesLowToHigh(final Collection<? extends Grade> grades) {
        return rankGrades(grades, comparing(Grade::getNumericValue).thenComparing(Grade::getTimestamp));
    }
}
