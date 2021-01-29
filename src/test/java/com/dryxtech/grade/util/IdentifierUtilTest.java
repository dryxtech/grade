package com.dryxtech.grade.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdentifierUtilTest {

    @Test
    void generateIdString() {
        String id = IdentifierUtil.generateIdString();

        assertEquals(32, id.length());
        assertTrue(id.matches("^[a-zA-Z0-9]+$"));
    }
}
