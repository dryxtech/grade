package com.dryxtech.grade.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.dryxtech.grade.GradeTesting.GRADE_REFERENCE_DESCRIPTION;
import static com.dryxtech.grade.GradeTesting.GRADE_REFERENCE_EXTENSIONS;
import static com.dryxtech.grade.GradeTesting.GRADE_REFERENCE_ID;
import static com.dryxtech.grade.GradeTesting.GRADE_REFERENCE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicGradeReferenceTest {

    private BasicGradeReference reference;

    @BeforeEach
    void setUp() {
        this.reference = new BasicGradeReference(
                GRADE_REFERENCE_ID,
                GRADE_REFERENCE_TYPE,
                GRADE_REFERENCE_DESCRIPTION,
                GRADE_REFERENCE_EXTENSIONS
        );
    }

    @Test
    void getId() {
        assertEquals(GRADE_REFERENCE_ID, reference.getId());
    }

    @Test
    void getType() {
        assertEquals(GRADE_REFERENCE_TYPE, reference.getType());
    }

    @Test
    void getDescription() {
        assertEquals(GRADE_REFERENCE_DESCRIPTION, reference.getDescription());
    }

    @Test
    void getExtensions() {
        assertEquals(GRADE_REFERENCE_EXTENSIONS, reference.getExtensions());
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(BasicGradeReference.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(reference.hashCode(), reference.hashCode());
        assertNotEquals(reference.hashCode(), new BasicGradeReference(
                GRADE_REFERENCE_ID,
                GRADE_REFERENCE_TYPE + "X",
                GRADE_REFERENCE_DESCRIPTION,
                GRADE_REFERENCE_EXTENSIONS
        ).hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(reference.toString());
        assertTrue(reference.toString().trim().length() > 0);
    }
}
