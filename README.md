[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

# DRYXTECH Grade

> The general-purpose grading library written for Java

## Features

* An API for Grades, Grading Systems, Graders and Grade Conversion and Management
* Basic models, builders and control implementation provided
* Common grading-systems included (standard academic, pass-fail and rating-type systems)
* Common graders included (standard numeric, average and weighted-average graders)
* Designed to easily extend for custom needs
    * Custom grading-systems can be defined in JSON Objects/Files for easy import
    * Custom graders and conversions can be easily constructed
    * Grade and reference types provide an extensions map for holding custom data
    * Does NOT try to assume the type of things being graded
* Helper classes included for common grading tasks (like ranking, averaging and distribution)

## Usage

### Minimum Requirements

Java 1.8 or above

### Basic Concepts

* **GradeValueRange** is a named numeric range (e.g. {80...100} = A)
* **GradingSystem** is a named set of grade value ranges (e.g. [{range},{...}] = FooGradingSystem)
* **GradingSystemRegistry** is a group of grading systems where each is registered under a name
* **GradeValue** is an object containing the numeric and text value of a grade (also includes grading system used)
* **Grader** is a functional interface that takes a specific input type and returns a grade value
* **Grade** is an object containing the graded value for a thing along with meta-data and references
* **GradeReference** is an object containing minimal identification information about some related thing
* **GradeConverter** is an interface to convert one grade value to another using a different grading system

### Basic Examples

#### Load a grading system from json file

```java
  GradingSystem gradingSystem = GradingSystemBuilder.builder(inputStream).build();
```

#### Determine the grade value of a given numeric input

```java
  Grader<Number> grader = new NumberGrader(gradingSystem);
  GradeValue gradeValue = grader.grade(92.5);
```

#### Create a grade

```java
  GradeBuilder gradeBuilder = GradeBuilder.builder(true); // auto-generate grade id
  GradeReferenceBuilder refBuilder = GradeReferenceBuilder.builder();

  Grade examGrade=gradeBuilder
    .type("student-final-exam")
    .timestamp(ZonedDateTime.now())
    .gradeValue(gradeValue)
    .extension("subject","BIO-111")
    .extension("semester","Spring 2021")
    .reference("tested",refBuilder.id("SID-00987654321").type("student").description("AJ Tivo").build())
    .reference("test",refBuilder.id("TID-123456").type("exam").description("BIO-111 Final Exam").build())
    .build();
```

#### Rollup weighted grades

```java
    Grader<Collection<Grade>> rollupGrader = new GradeWeightedAverageGrader(gradingSystem);

    GradeValue classGradeValue = rollupGrader.grade(Arrays.asList(
      gradeBuilder.type("final-exam").gradeValue(examGrade).weight(.30).build(),
      gradeBuilder.type("homework-average").gradeValue(grader.grade(100)).weight(.60).build(),
      gradeBuilder.type("participation").gradeValue(grader.grade(50)).weight(.10).build())
    );
```

#### Convert grade value to another grading system

```java
    GradingSystemRegistry registry = new GradingSystemRegistry();
    registry.registerSystem("gpa_grading_system",gpaGradingSystem);

    GradeConverter converter = new TextValueBasedConverter(registry);
    GradeValue gpaGradeValue = converter.convert(classGradeValue,"gpa_grading_system");
```

### Advanced Uses

* **ManagedGrade** An extended Grade Object which includes Management Information
* **GradeBook** An interface to a collection of managed grades (typically an in-memory grade store)
* **GradeRank** A value object containing a grade and a rank
* **GradeManager** A controller class over the grading process that provides both convenience methods and control over
  integrated parts

### Advanced Examples

#### Create a managed grade and record in a GradeBook

```java
    ManagedGrade managedGrade = new BasicManagedGrade(gpaGrade,Collections.singletonMap("organization","DRYXTECH"));

    GradeBook<ManagedGrade> gradeBook = new SimpleMemoryGradeBook<>();
    gradeBook.record(managedGrade);
```

#### Print the Top 10 GPAs in a GradeBook

```java
    Collection<ManagedGrade> allGpaGrades = gradeBook.find(mgrade->
        mgrade.getType().equals("student-gpa") &&
        mgrade.getManagement().get("organization").equals("DRYXTECH")
    );

    GradeMathUtil.rankGradesHiToLow(allGpaGrades).subList(0,10).forEach(gradeRank -> {
        System.out.println(gradeRank.getRank() + " " + gradeRank.getGrade().getReference("student"));
    });
```

#### Simple use of a grade manager to grade and record a value using a bundled grading system

```java
    GradeManager manager = new GradeManager(
        new GradingSystemRegistry(),
        new SimpleMemoryGradeBook<ManagedGrade>(),
        Collections.singletonMap("organization","DRYXTECH")
    );

    manager.loadBundledGradingSystems();

    manager.setDefaultGradingSystem(manager.lookupSystem(STANDARD_PLUS_MINUS_ACADEMIC_SYSTEM));

    manager.record(manager.getGradeBuilder(true).type("example-grade").gradeValue(manager.grade(100)).build());
```

### Grading Systems

There are standard grading system definition files in the resources/grading-systems folder that are included in the
built-artifact. They can be individually loaded using the GradeFileUtil; or one can use the GradeManager to load all
bundled grading systems.

The file type is a json and must include the following fields:  
```id, description, category, type, name, variant and ranges ```

Each range must include the following fields:  
```textValue, startValue, startValueInclusive, endValue, EndValueInclusive, performanceLevel```

#### Example Grading System Definition File

```json
{
  "id": "general.percent.pass-fail.us",
  "description": "A percent-based grading system where Pass is 100% otherwise Fail",
  "category": "general",
  "type": "percent",
  "name": "pass-fail",
  "variant": "us",
  "ranges": [
    {
      "textValue": "PASS",
      "startValue": 100,
      "startValueInclusive": true,
      "endValue": 100,
      "endValueInclusive": true,
      "performanceLevel": "Top"
    },
    {
      "textValue": "FAIL",
      "startValue": 0,
      "startValueInclusive": true,
      "endValue": 100,
      "endValueInclusive": false,
      "performanceLevel": "Bottom"
    }
  ]
}
```

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.  
If you believe you found a bug or have any question, please use the issue tracker.

## License

[Apache 2.0](./LICENSE)
