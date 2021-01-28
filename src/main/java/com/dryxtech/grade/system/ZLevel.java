package com.dryxtech.grade.system;

import com.dryxtech.grade.model.BasicGradeValueRange;
import com.dryxtech.grade.model.GradeValueRangeBuilder;
import com.dryxtech.grade.api.PerformanceLevel;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

/**
 * An enum of grade ranges for the Z Grading System
 *
 * @author Drew Griffin
 * @since 1.0
 */
public enum ZLevel {

    TOP_PLUS("VP", 1, GradeValueRangeBuilder.builder().rangeStartValue(101).isRangeStartInclusive(true)
            .rangeEndValue(Integer.MAX_VALUE).isRangeEndInclusive(true).textValue("VP").performanceLevel(PerformanceLevel.TOP).build()),
    TOP("V", 2, GradeValueRangeBuilder.builder().rangeStartValue(100).isRangeStartInclusive(true)
            .rangeEndValue(101).isRangeEndInclusive(false).textValue("V").performanceLevel(PerformanceLevel.TOP).build()),
    TOP_MINUS("VM", 3, GradeValueRangeBuilder.builder().rangeStartValue(99).isRangeStartInclusive(true)
            .rangeEndValue(100).isRangeEndInclusive(false).textValue("VM").performanceLevel(PerformanceLevel.TOP).build()),
    HIGH_PLUS("WP", 4, GradeValueRangeBuilder.builder().rangeStartValue(90).isRangeStartInclusive(true)
            .rangeEndValue(99).isRangeEndInclusive(false).textValue("WP").performanceLevel(PerformanceLevel.HIGH).build()),
    HIGH("W", 5, GradeValueRangeBuilder.builder().rangeStartValue(80).isRangeStartInclusive(true)
            .rangeEndValue(90).isRangeEndInclusive(false).textValue("W").performanceLevel(PerformanceLevel.HIGH).build()),
    HIGH_MINUS("WM", 6, GradeValueRangeBuilder.builder().rangeStartValue(70).isRangeStartInclusive(true)
            .rangeEndValue(80).isRangeEndInclusive(false).textValue("WM").performanceLevel(PerformanceLevel.HIGH).build()),
    MEDIUM_PLUS("XP", 7, GradeValueRangeBuilder.builder().rangeStartValue(60).isRangeStartInclusive(true)
            .rangeEndValue(70).isRangeEndInclusive(false).textValue("XP").performanceLevel(PerformanceLevel.MEDIUM_HIGH).build()),
    MEDIUM("X", 8, GradeValueRangeBuilder.builder().rangeStartValue(50).isRangeStartInclusive(true)
            .rangeEndValue(60).isRangeEndInclusive(false).textValue("X").performanceLevel(PerformanceLevel.MEDIUM).build()),
    MEDIUM_MINUS("XM", 9, GradeValueRangeBuilder.builder().rangeStartValue(40).isRangeStartInclusive(true)
            .rangeEndValue(50).isRangeEndInclusive(false).textValue("XM").performanceLevel(PerformanceLevel.MEDIUM_LOW).build()),
    LOW_PLUS("YP", 10, GradeValueRangeBuilder.builder().rangeStartValue(30).isRangeStartInclusive(true)
            .rangeEndValue(40).isRangeEndInclusive(false).textValue("YP").performanceLevel(PerformanceLevel.LOW).build()),
    LOW("Y", 11, GradeValueRangeBuilder.builder().rangeStartValue(20).isRangeStartInclusive(true)
            .rangeEndValue(30).isRangeEndInclusive(false).textValue("Y").performanceLevel(PerformanceLevel.LOW).build()),
    LOW_MINUS("YM", 12, GradeValueRangeBuilder.builder().rangeStartValue(10).isRangeStartInclusive(true)
            .rangeEndValue(20).isRangeEndInclusive(false).textValue("YM").performanceLevel(PerformanceLevel.LOW).build()),
    BOTTOM_PLUS("ZP", 13, GradeValueRangeBuilder.builder().rangeStartValue(1).isRangeStartInclusive(true)
            .rangeEndValue(10).isRangeEndInclusive(false).textValue("ZP").performanceLevel(PerformanceLevel.BOTTOM).build()),
    BOTTOM("Z", 14, GradeValueRangeBuilder.builder().rangeStartValue(0).isRangeStartInclusive(true)
            .rangeEndValue(1).isRangeEndInclusive(false).textValue("Z").performanceLevel(PerformanceLevel.BOTTOM).build()),
    BOTTOM_MINUS("ZM", 15, GradeValueRangeBuilder.builder().rangeStartValue(Integer.MAX_VALUE * -1).isRangeStartInclusive(true)
            .rangeEndValue(0).isRangeEndInclusive(false).textValue("ZM").performanceLevel(PerformanceLevel.BOTTOM).build());

    private static final ZLevel[] ORDERED_LEVELS = new ZLevel[]{BOTTOM_MINUS, BOTTOM, BOTTOM_PLUS, LOW_MINUS,
            LOW, LOW_PLUS, MEDIUM_MINUS, MEDIUM, MEDIUM_PLUS, HIGH_MINUS, HIGH, HIGH_PLUS, TOP_MINUS, TOP, TOP_PLUS};
    private final String textValue;
    private final int order;
    private final BasicGradeValueRange range;

    ZLevel(String textValue, int order, BasicGradeValueRange range) {
        this.textValue = textValue;
        this.order = order;
        this.range = range;
    }

    public static Optional<ZLevel> of(Number numericValue) {

        if (Objects.nonNull(numericValue)) {
            int intValue = numericValue.intValue();
            int startIndex = 0;
            if (intValue >= 90) {
                startIndex = ORDERED_LEVELS.length - 5;
            } else if (intValue >= 10) {
                startIndex = intValue / 10;
            }

            BigDecimal value = new BigDecimal(numericValue.toString());
            for (int i=startIndex; i<ORDERED_LEVELS.length; i++) {
                if (ORDERED_LEVELS[i].getRange().inRange(value)) {
                    return Optional.of(ORDERED_LEVELS[i]);
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<ZLevel> of(String textValue) {

        switch (textValue.toUpperCase()) {

            case "VP":
                return Optional.of(TOP_PLUS);
            case "V":
                return Optional.of(TOP);
            case "VM":
                return Optional.of(TOP_MINUS);
            case "WP":
                return Optional.of(HIGH_PLUS);
            case "W":
                return Optional.of(HIGH);
            case "WM":
                return Optional.of(HIGH_MINUS);
            case "XP":
                return Optional.of(MEDIUM_PLUS);
            case "X":
                return Optional.of(MEDIUM);
            case "XM":
                return Optional.of(MEDIUM_MINUS);
            case "YP":
                return Optional.of(LOW_PLUS);
            case "Y":
                return Optional.of(LOW);
            case "YM":
                return Optional.of(LOW_MINUS);
            case "ZP":
                return Optional.of(BOTTOM_PLUS);
            case "Z":
                return Optional.of(BOTTOM);
            case "ZM":
                return Optional.of(BOTTOM_MINUS);
            default:
                return Optional.empty();
        }
    }

    public String getTextValue() {
        return textValue;
    }

    public int getOrder() {
        return order;
    }

    public BasicGradeValueRange getRange() {
        return range;
    }
}
