package com.dryxtech.grade.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static com.dryxtech.grade.GradeTesting.GRADE_DESCRIPTION;
import static com.dryxtech.grade.GradeTesting.GRADE_EXTENSIONS;
import static com.dryxtech.grade.GradeTesting.GRADE_GRADING_SYSTEM;
import static com.dryxtech.grade.GradeTesting.GRADE_ID;
import static com.dryxtech.grade.GradeTesting.GRADE_NUMERIC_VALUE;
import static com.dryxtech.grade.GradeTesting.GRADE_REFERENCES;
import static com.dryxtech.grade.GradeTesting.GRADE_TEXT_VALUE;
import static com.dryxtech.grade.GradeTesting.GRADE_TIMESTAMP;
import static com.dryxtech.grade.GradeTesting.GRADE_TYPE;
import static com.dryxtech.grade.GradeTesting.GRADE_WEIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicGradeTest {

    private BasicGrade grade;

    @BeforeEach
    void setUp() {
        this.grade = new BasicGrade(
                GRADE_ID,
                GRADE_TYPE,
                GRADE_DESCRIPTION,
                GRADE_EXTENSIONS,
                GRADE_NUMERIC_VALUE,
                GRADE_TEXT_VALUE,
                GRADE_GRADING_SYSTEM,
                GRADE_TIMESTAMP,
                GRADE_WEIGHT,
                GRADE_REFERENCES
        );
    }

    @Test
    void getGradingSystem() {
        assertEquals(GRADE_GRADING_SYSTEM, grade.getGradingSystem());
    }

    @Test
    void getTimestamp() {
        assertEquals(GRADE_TIMESTAMP, grade.getTimestamp());
    }

    @Test
    void getWeight() {
        assertEquals(GRADE_WEIGHT, grade.getWeight());
    }

    @Test
    void getReference() {
        assertEquals(Optional.of(GRADE_REFERENCES.get("test")), grade.getReference("test"));
        assertEquals(Optional.empty(), grade.getReference("NOT_VALID_KEY"));
        assertNotEquals(Optional.empty(), grade.getReference("test"));
    }

    @Test
    void getReferences() {
        assertEquals(GRADE_REFERENCES, grade.getReferences());
    }

    @Test
    void getId() {
        assertEquals(GRADE_ID, grade.getId());
    }

    @Test
    void getType() {
        assertEquals(GRADE_TYPE, grade.getType());
    }

    @Test
    void getDescription() {
        assertEquals(GRADE_DESCRIPTION, grade.getDescription());
    }

    @Test
    void getExtension() {
        assertEquals(Optional.of(GRADE_EXTENSIONS.get("semester")), grade.getExtension("semester"));
        assertEquals(Optional.empty(), grade.getExtension("NOT_VALID_KEY"));
        assertNotEquals(Optional.empty(), grade.getExtension("semester"));
    }

    @Test
    void getExtensions() {
        assertEquals(GRADE_EXTENSIONS, grade.getExtensions());
    }

    @Test
    void getNumericValue() {
        assertEquals(GRADE_NUMERIC_VALUE, grade.getNumericValue());
    }

    @Test
    void getTextValue() {
        assertEquals(GRADE_TEXT_VALUE, grade.getTextValue());
    }

    @Test
    void compareTo() {
        assertEquals(-1, grade.compareTo(new BasicGradeValue(grade.getNumericValue().add(BigDecimal.ONE), GRADE_TEXT_VALUE, GRADE_GRADING_SYSTEM)));
        assertEquals(0, grade.compareTo(new BasicGradeValue(grade.getNumericValue(), GRADE_TEXT_VALUE, GRADE_GRADING_SYSTEM)));
        assertEquals(1, grade.compareTo(new BasicGradeValue(grade.getNumericValue().subtract(BigDecimal.ONE), GRADE_TEXT_VALUE, GRADE_GRADING_SYSTEM)));
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(BasicGrade.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(grade.hashCode(), grade.hashCode());
        assertNotEquals(grade.hashCode(), new BasicGrade(
                GRADE_ID,
                GRADE_TYPE,
                GRADE_DESCRIPTION,
                GRADE_EXTENSIONS,
                GRADE_NUMERIC_VALUE.add(BigDecimal.ONE),
                GRADE_TEXT_VALUE,
                GRADE_GRADING_SYSTEM,
                GRADE_TIMESTAMP,
                GRADE_WEIGHT,
                GRADE_REFERENCES
        ).hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(grade.toString());
        assertTrue(grade.toString().trim().length() > 0);
    }
}
