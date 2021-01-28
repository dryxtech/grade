package com.dryxtech.grade.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.dryxtech.grade.GradeTesting.GRADE_GRADING_SYSTEM;
import static com.dryxtech.grade.GradeTesting.GRADE_NUMERIC_VALUE;
import static com.dryxtech.grade.GradeTesting.GRADE_TEXT_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicGradeValueTest {

    private BasicGradeValue value;

    @BeforeEach
    void setUp() {
        this.value = new BasicGradeValue(
                GRADE_NUMERIC_VALUE,
                GRADE_TEXT_VALUE,
                GRADE_GRADING_SYSTEM
        );
    }

    @Test
    void getNumericValue() {
        assertEquals(GRADE_NUMERIC_VALUE, value.getNumericValue());
    }

    @Test
    void getTextValue() {
        assertEquals(GRADE_TEXT_VALUE, value.getTextValue());
    }

    @Test
    void getGradingSystem() {
        assertEquals(GRADE_GRADING_SYSTEM, value.getGradingSystem());
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(BasicGradeValue.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(value.hashCode(), value.hashCode());
        assertNotEquals(value.hashCode(), new BasicGradeValue(
                GRADE_NUMERIC_VALUE.add(BigDecimal.ONE),
                GRADE_TEXT_VALUE,
                GRADE_GRADING_SYSTEM
        ).hashCode());
    }

    @Test
    void compareTo() {
        assertEquals(-1, value.compareTo(null));
        assertEquals(-1, value.compareTo(new BasicGradeValue(
                GRADE_NUMERIC_VALUE.add(BigDecimal.ONE),
                GRADE_TEXT_VALUE,
                GRADE_GRADING_SYSTEM
        )));
        assertEquals(0, value.compareTo(value));
        assertEquals(1, value.compareTo(new BasicGradeValue(
                GRADE_NUMERIC_VALUE.subtract(BigDecimal.ONE),
                GRADE_TEXT_VALUE,
                GRADE_GRADING_SYSTEM
        )));
    }

    @Test
    void testToString() {
        assertNotNull(value.toString());
        assertTrue(value.toString().trim().length() > 0);
    }
}
