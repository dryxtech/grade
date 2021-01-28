package com.dryxtech.grade.control;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GradeConstantsTest {

    /**
     * Standard grading system ids follow a strict 4 part format
     * category.type.name.variant
     */
    @Test
    public void gradingSystemIds_Format() {
        Arrays.stream(GradeConstants.GRADING_SYSTEM_IDS).forEach(id -> {
            String[] parts = id.split("\\.");
            assertEquals(4, parts.length);
        });
    }
}
