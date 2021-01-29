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

package com.dryxtech.grade.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GradeExceptionTest {

    @Test
    public void testConstructor_Message() {

        String message = "test";

        Exception exception = assertThrows(GradeException.class, () -> {
            throw new GradeException(message);
        });
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testConstructor_Null() {

        Exception exception = assertThrows(GradeException.class, () -> {
            throw new GradeException(null);
        });
        assertNull(exception.getMessage());
    }

    @Test
    public void testConstructor_MessageAndCause() {

        String message = "test";
        Throwable cause = new IllegalStateException("foobar");

        Exception exception = assertThrows(GradeException.class, () -> {
            throw new GradeException(message, cause);
        });
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testConstructor_MessageAndNull() {

        String message = "test";

        Exception exception = assertThrows(GradeException.class, () -> {
            throw new GradeException(message, null);
        });
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
}
