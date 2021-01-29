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
