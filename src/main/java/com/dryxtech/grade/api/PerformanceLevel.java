package com.dryxtech.grade.api;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * This enum represents standard performance levels
 *
 * @author Drew Griffin
 * @since 1.0
 */
public enum PerformanceLevel {

    TOP("Top"),
    HIGH("High"),
    MEDIUM_HIGH("Medium High"),
    MEDIUM("Medium"),
    MEDIUM_LOW("Medium Low"),
    LOW("Low"),
    BOTTOM("Bottom"),
    NONE("");

    private final String label;

    PerformanceLevel(String label) {
        this.label = label;
    }

    public static PerformanceLevel of(String label) {

        if (Objects.isNull(label)) {
            return NONE;
        }

        switch (label.toUpperCase()) {

            case "TOP":
                return TOP;

            case "HIGH":
                return HIGH;

            case "MEDIUM HIGH":
                return MEDIUM_HIGH;

            case "MEDIUM":
                return MEDIUM;

            case "MEDIUM LOW":
                return MEDIUM_LOW;

            case "LOW":
                return LOW;

            case "BOTTOM":
                return BOTTOM;

            default:
                return NONE;
        }
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
