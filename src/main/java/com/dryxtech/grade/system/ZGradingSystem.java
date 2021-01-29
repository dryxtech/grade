package com.dryxtech.grade.system;

import com.dryxtech.grade.control.GradeConstants;
import com.dryxtech.grade.model.BasicGradingSystem;

import java.util.Arrays;

/**
 * The Z Grading System
 *
 * @author Drew Griffin
 * @since 1.0
 */
public final class ZGradingSystem extends BasicGradingSystem {

    public static final String DESCRIPTION = "A percent-based grading system consisting of five primary levels (V to Z)";
    public static final String CATEGORY = "general";
    public static final String TYPE = GradingSystemType.PERCENT.getLabel();
    public static final String NAME = "z-system";
    public static final String VARIANT = "us";

    public ZGradingSystem() {
        super(GradeConstants.Z_GRADING_SYSTEM, DESCRIPTION, CATEGORY, TYPE, NAME, VARIANT, Arrays.asList(
                ZLevel.TOP_PLUS.getRange(),
                ZLevel.TOP.getRange(),
                ZLevel.TOP_MINUS.getRange(),
                ZLevel.HIGH_PLUS.getRange(),
                ZLevel.HIGH.getRange(),
                ZLevel.HIGH_MINUS.getRange(),
                ZLevel.MEDIUM_PLUS.getRange(),
                ZLevel.MEDIUM.getRange(),
                ZLevel.MEDIUM_MINUS.getRange(),
                ZLevel.LOW_PLUS.getRange(),
                ZLevel.LOW.getRange(),
                ZLevel.LOW_MINUS.getRange(),
                ZLevel.BOTTOM_PLUS.getRange(),
                ZLevel.BOTTOM.getRange(),
                ZLevel.BOTTOM_MINUS.getRange())
        );
    }
}
