package com.dryxtech.grade.system;

public enum GradingSystemType {

    BINARY("binary"),
    GPA("gpa"),
    PERCENT("percent"),
    RATING("rating"),
    TERNARY("ternary");

    private final String label;

    GradingSystemType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
