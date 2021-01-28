package com.dryxtech.grade.system.converter;

import com.dryxtech.grade.model.BasicGradeValue;
import com.dryxtech.grade.api.GradeConverter;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.system.GradingSystemRegistry;
import com.dryxtech.grade.system.ZLevel;
import com.dryxtech.grade.system.ZGradingSystem;
import com.dryxtech.grade.util.GradeFileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ZSystemToFiveStarConverterTest {

    private GradeConverter converter;
    private GradingSystem fromSystem;
    private GradingSystem toSystem;

    @BeforeEach
    void setUp() throws IOException {
        this.fromSystem = new ZGradingSystem();
        this.toSystem = GradeFileUtil.loadBundledGradingSystem(GradeConstants.STANDARD_FIVE_STAR_SYSTEM + GradeConstants.GRADING_SYSTEM_FILE_EXTENSION);

        GradingSystemRegistry registry = new GradingSystemRegistry();
        registry.registerSystem(fromSystem.getId(), fromSystem);
        registry.registerSystem(toSystem.getId(), toSystem);

        converter = new ZSystemToFiveStarConverter(registry);
        registry.registerConverter(fromSystem.getId(), toSystem.getId(), converter);
    }

    @Test
    void convert() {

        ZLevel zGradeLevel = ZLevel.MEDIUM;
        GradeValue originalValue = new BasicGradeValue(zGradeLevel.getRange().getEndValue(), zGradeLevel.getTextValue(), fromSystem.getId());
        GradeValue convertedValue = assertDoesNotThrow(() -> converter.convert(originalValue, toSystem.getId()));

        assertEquals(3, convertedValue.getNumericValue().intValue());
        assertEquals("3-STAR", convertedValue.getTextValue());
    }
}
