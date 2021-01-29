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

import com.dryxtech.grade.api.ManagedGrade;
import com.dryxtech.grade.model.BasicManagedGrade;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SimpleMemoryGradeBookTest {

    private List<ManagedGrade> rawList;
    private SimpleMemoryGradeBook<ManagedGrade> gradeBook;

    @Mock
    private ManagedGrade tooHotGrade;

    @Mock
    private ManagedGrade tooColdGrade;

    @Mock
    private ManagedGrade justRightGrade;

    @BeforeEach
    void setup() {

        Mockito.lenient().when(tooHotGrade.getId()).thenReturn("too hot");
        Mockito.lenient().when(tooColdGrade.getId()).thenReturn("too cold");
        Mockito.lenient().when(justRightGrade.getId()).thenReturn("just right");

        this.rawList = new ArrayList<>();
        this.rawList.add(tooHotGrade);
        this.rawList.add(tooColdGrade);
        this.rawList.add(justRightGrade);

        this.gradeBook = new SimpleMemoryGradeBook<>(SimpleMemoryGradeBookTest.class.getSimpleName(), rawList, 4);
    }

    @Test
    void getName() {
        assertEquals(SimpleMemoryGradeBookTest.class.getSimpleName(), gradeBook.getName());
    }

    @Test
    void recordGrade() {

        ManagedGrade grade = Mockito.mock(BasicManagedGrade.class);

        gradeBook.record(grade);

        assertEquals(4, rawList.size());
        assertTrue(rawList.contains(grade));

        // at max limit
        assertThrows(IllegalStateException.class, () -> gradeBook.record(grade));
    }

    @Test
    void recordGrades() {

        ManagedGrade grade = Mockito.mock(BasicManagedGrade.class);

        gradeBook.record(Collections.singletonList(grade));

        assertEquals(4, rawList.size());
        assertTrue(rawList.contains(grade));

        // at max limit
        assertThrows(IllegalStateException.class, () -> gradeBook.record(Collections.singletonList(grade)));
    }

    @Test
    void eraseGrade() {

        boolean result = gradeBook.erase(tooHotGrade);

        assertTrue(result);
        assertEquals(2, rawList.size());
        assertFalse(rawList.contains(tooHotGrade));
    }

    @Test
    void eraseGrades() {

        Collection<ManagedGrade> erasedGrades = gradeBook.erase(grade -> !grade.getId().equals("just right"));

        assertEquals(1, rawList.size());
        assertFalse(rawList.contains(tooHotGrade));
        assertFalse(rawList.contains(tooColdGrade));

        assertEquals(2, erasedGrades.size());
        assertTrue(erasedGrades.contains(tooHotGrade));
        assertTrue(erasedGrades.contains(tooColdGrade));
    }

    @Test
    void findGrades() {

        Collection<ManagedGrade> foundGrades = gradeBook.find(grade -> !grade.getId().equals("just right"));

        assertEquals(2, foundGrades.size());
        assertTrue(foundGrades.contains(tooHotGrade));
        assertTrue(foundGrades.contains(tooColdGrade));
    }

    @Test
    void findAllGrades() {

        List<ManagedGrade> gradesFromStream = new ArrayList<>(gradeBook.findAll());

        assertEquals(3, gradesFromStream.size());
        assertTrue(gradesFromStream.contains(tooHotGrade));
        assertTrue(gradesFromStream.contains(tooColdGrade));
        assertTrue(gradesFromStream.contains(justRightGrade));
    }

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(BasicManagedGrade.class).verify();
    }

    @Test
    void testHashCode() {
        assertEquals(gradeBook.hashCode(), gradeBook.hashCode());
        assertNotEquals(gradeBook.hashCode(), new SimpleMemoryGradeBook<>().hashCode());
    }

    @Test
    void testToString() {

        assertNotNull(gradeBook.toString());
        assertTrue(gradeBook.toString().trim().length() > 0);
    }
}
