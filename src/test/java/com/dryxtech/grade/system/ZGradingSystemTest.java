package com.dryxtech.grade.system;

import nl.jqno.equalsverifier.EqualsVerifier;
import com.dryxtech.grade.GradeTesting;
import com.dryxtech.grade.api.GradeValueRange;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.control.GradeConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ZGradingSystemTest {

    private GradingSystem gradingSystem;

    @BeforeEach
    void setUp() {
        gradingSystem = new ZGradingSystem();
    }

    @Test
    void getId() {
        assertEquals(GradeConstants.Z_GRADING_SYSTEM, gradingSystem.getId());
    }

    @Test
    void getDescription() {
        assertEquals(ZGradingSystem.DESCRIPTION, gradingSystem.getDescription());
    }

    @Test
    void getCategory() {
        assertEquals(ZGradingSystem.CATEGORY, gradingSystem.getCategory());
    }

    @Test
    void getType() {
        assertEquals(ZGradingSystem.TYPE, gradingSystem.getType());
    }

    @Test
    void getName() {
        assertEquals(ZGradingSystem.NAME, gradingSystem.getName());
    }

    @Test
    void getVariant() {
        assertEquals(ZGradingSystem.VARIANT, gradingSystem.getVariant());
    }

    @Test
    void getRanges() {
        List<GradeValueRange> ranges = new ArrayList<>();
        gradingSystem.getRanges().forEach(range -> {
            assertNotNull(range);
            assertFalse(ranges.contains(range));
            ranges.add(range);
        });
        assertFalse(ranges.isEmpty());
    }

    @Test
    void getStartNumericValue() {
        gradingSystem.getRanges().forEach(range -> {
            assertEquals(ZLevel.of(range.getTextValue()).get().getRange().getEffectiveRangeStartValue(),
                    gradingSystem.getStartNumericValue(range.getTextValue()).get());
        });
    }

    @Test
    void getMidNumericValue() {
        gradingSystem.getRanges().forEach(range -> {
            assertEquals(ZLevel.of(range.getTextValue()).get().getRange().getEffectiveRangeMidValue(),
                    gradingSystem.getMidNumericValue(range.getTextValue()).get());
        });
    }

    @Test
    void getEndNumericValue() {
        gradingSystem.getRanges().forEach(range -> {
            assertEquals(ZLevel.of(range.getTextValue()).get().getRange().getEffectiveRangeEndValue(),
                    gradingSystem.getEndNumericValue(range.getTextValue()).get());
        });
    }

    @Test
    void getTextValue() {
        gradingSystem.getRanges().forEach(range -> {
            assertEquals(ZLevel.of(range.getTextValue()).get().getRange().getEffectiveRangeStartValue(),
                    gradingSystem.getStartNumericValue(range.getTextValue()).get());
        });
    }

    @Test
    void getPerformanceLevel() {
        gradingSystem.getRanges().forEach(range -> {
            assertEquals(Optional.of(range.getPerformanceLevel()),
                    gradingSystem.getPerformanceLevel(range.getEffectiveRangeMidValue()));
        });
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(ZGradingSystem.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(gradingSystem.hashCode(), gradingSystem.hashCode());
        assertNotEquals(gradingSystem.hashCode(), GradeTesting.mockGradingSystem().hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(gradingSystem.toString());
        assertTrue(gradingSystem.toString().trim().length() > 0);
    }
}
