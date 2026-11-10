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

    @Test
    void letterGradeBelowZeroThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.letterGrade(-0.01));
    }

    @Test
    void letterGradeAboveHundredThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.letterGrade(100.01));
    }

    @Test
    void letterGradeC() {
        assertEquals("C", calculator.letterGrade(75));
    }

    @Test
    void letterGradeD() {
        assertEquals("D", calculator.letterGrade(65));
    }

    @Test
    void letterGradeF() {
        assertEquals("F", calculator.letterGrade(55));
    }

    @Test
    void gpaNullThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.gpa(null));
    }

    @Test
    void gpaEmptyThrows() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.gpa(new double[]{}));
    }

    @Test
    void gpaCoversAllGradeSwitchBranches() {
        assertEquals(
                2.0,
                calculator.gpa(
                        new double[]{95, 85, 75, 65, 55}),
                0.0001);
    }

    @Test
    void scholarshipInvalidGpaBelowZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.scholarship(-0.1, 12, false));
    }

    @Test
    void scholarshipInvalidGpaAboveFour() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.scholarship(4.1, 12, false));
    }

    @Test
    void scholarshipInvalidCredits() {
        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.scholarship(3.5, -1, false));
    }

    @Test
    void scholarshipViolationGetsZero() {
        assertEquals(
                0,
                calculator.scholarship(4.0, 20, true));
    }

    @Test
    void scholarshipHundredCreditShortfallGets70() {
        assertEquals(
                70,
                calculator.scholarship(3.9, 14, false));
    }

    @Test
    void scholarshipSeventyCreditShortfallGets50() {
        assertEquals(
                50,
                calculator.scholarship(3.5, 11, false));
    }

    @Test
    void scholarshipGpaBelow34Gets50() {
        assertEquals(
                50,
                calculator.scholarship(3.2, 20, false));
    }

    @Test
    void scholarshipBelow30GetsZero() {
        assertEquals(
                0,
                calculator.scholarship(2.9, 20, false));
    }
}
