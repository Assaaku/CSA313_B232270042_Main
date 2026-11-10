package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ScholarshipCalculatorTest {

    private final ScholarshipCalculator calculator =
            new ScholarshipCalculator();

    @Test
    void happyLetterGradeA() {
        assertEquals("A", calculator.letterGrade(95));
    }

    @Test
    void happyLetterGradeB() {
        assertEquals("B", calculator.letterGrade(85));
    }

    @Test
    void happyGpa() {
        assertEquals(
                3.5,
                calculator.gpa(new double[]{95, 85}),
                0.0001);
    }

    @Test
    void happyScholarship100() {
        assertEquals(
                100,
                calculator.scholarship(3.9, 16, false));
    }

    @Test
    void happyScholarship70() {
        assertEquals(
                70,
                calculator.scholarship(3.5, 12, false));
    }
}
