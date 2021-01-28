package com.dryxtech.grade;

import com.dryxtech.grade.model.BasicGradeReference;
import com.dryxtech.grade.system.GradingSystemType;
import com.dryxtech.grade.api.Grade;
import com.dryxtech.grade.api.GradeReference;
import com.dryxtech.grade.api.GradeValue;
import com.dryxtech.grade.api.GradingSystem;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public final class GradeTesting {

    public static final String GRADING_SYSTEM_ID = UUID.randomUUID().toString();
    public static final String GRADING_SYSTEM_TYPE = GradingSystemType.PERCENT.getLabel();
    public static final String GRADE_REFERENCE_ID = UUID.randomUUID().toString();
    public static final String GRADE_REFERENCE_TYPE = "test";
    public static final String GRADE_REFERENCE_DESCRIPTION = "English 101 Final Exam Test";
    public static final Map<String, Object> GRADE_REFERENCE_EXTENSIONS = Collections.singletonMap("environment", "online");
    public static final String GRADE_ID = UUID.randomUUID().toString();
    public static final String GRADE_TYPE = "FINAL_EXAM_ENG101";
    public static final String GRADE_DESCRIPTION = "English 101 Final Exam for Student";
    public static final Map<String, Object> GRADE_EXTENSIONS = Collections.singletonMap("semester", "fall");
    public static final BigDecimal GRADE_NUMERIC_VALUE = new BigDecimal(100);
    public static final String GRADE_TEXT_VALUE = "A+";
    public static final String GRADE_GRADING_SYSTEM = "academic.percent.standard-plus-minus.us";
    public static final ZonedDateTime GRADE_TIMESTAMP = ZonedDateTime.now();
    public static final BigDecimal GRADE_WEIGHT = new BigDecimal("0.3");
    public static final Map<String, BasicGradeReference> GRADE_REFERENCES = Collections.singletonMap("test",
            new BasicGradeReference(GRADE_REFERENCE_ID, GRADE_REFERENCE_TYPE, GRADE_REFERENCE_DESCRIPTION, GRADE_REFERENCE_EXTENSIONS));
    public static final Map<String, Object> GRADE_MANAGEMENT = Collections.singletonMap("organization", "DRYXTECH");

    private GradeTesting() {
        // Test Utility
    }

    public static GradeValue mockGradeValue(Number numericValue, String textValue) {
        GradeValue mock = Mockito.mock(GradeValue.class);
        when(mock.getNumericValue()).thenReturn(new BigDecimal(numericValue.toString()));
        when(mock.getTextValue()).thenReturn(textValue);
        when(mock.getGradingSystem()).thenReturn("test-grading-system");
        return mock;
    }

    public static GradeReference mockGradeReference(String type, String description) {
        GradeReference mock = Mockito.mock(GradeReference.class);
        when(mock.getId()).thenReturn(UUID.randomUUID().toString());
        when(mock.getType()).thenReturn(type);
        when(mock.getDescription()).thenReturn(description);
        return mock;
    }

    public static Grade mockGrade(GradeReference self, GradeValue value, Number weight) {

        return mockGrade(self.getId(), self.getType(), self.getDescription(), self.getExtensions(),
                value.getNumericValue(), value.getTextValue(), value.getGradingSystem(), ZonedDateTime.now(),
                weight, Collections.emptyMap());
    }

    public static Grade mockGrade(String id, String type, String description, Map<String, Object> extensions,
                                  BigDecimal numericValue, String textValue, String gradingSystem,
                                  ZonedDateTime timestamp, Number weight, Map<String, GradeReference> references) {

        Grade mock = Mockito.mock(Grade.class);
        when(mock.getId()).thenReturn(id);
        when(mock.getType()).thenReturn(type);
        when(mock.getDescription()).thenReturn(description);
        when(mock.getExtensions()).thenReturn(extensions);
        when(mock.getNumericValue()).thenReturn(numericValue);
        when(mock.getTextValue()).thenReturn(textValue);
        when(mock.getGradingSystem()).thenReturn(gradingSystem);
        when(mock.getTimestamp()).thenReturn(timestamp);
        when(mock.getReferences()).thenReturn(references);
        when(mock.getWeight()).thenReturn(new BigDecimal(weight.toString()));
        return mock;
    }

    public static List<Grade> mockGrades(Number... numericValues) {
        List<Grade> mocks = new ArrayList<>();
        for (Number value : numericValues) {
            GradeValue gradeValue = mockGradeValue(value, (value.intValue() % 2 == 0) ? "EVEN" : "ODD");
            GradeReference self = mockGradeReference(GRADE_TYPE, GRADE_DESCRIPTION + value.intValue());
            Grade grade = mockGrade(self, gradeValue, 1);
            mocks.add(grade);
        }

        return mocks;
    }

    public static List<Grade> mockGrades(int start, int total) {
        List<Number> values = IntStream.range(start, start + total).boxed().collect(Collectors.toList());
        return mockGrades(values.toArray(new Number[total]));
    }

    public static GradingSystem mockGradingSystem() {
        GradingSystem mock = Mockito.mock(GradingSystem.class);
        when(mock.getId()).thenReturn(GRADING_SYSTEM_ID);
        when(mock.getType()).thenReturn(GRADING_SYSTEM_TYPE);
        when(mock.getDescription()).thenReturn("");
        when(mock.getCategory()).thenReturn("");
        when(mock.getTextValue(any())).thenReturn(Optional.of("T"));
        return mock;
    }
}
