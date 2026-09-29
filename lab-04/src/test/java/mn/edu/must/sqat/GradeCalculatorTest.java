package mn.edu.must.sqat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculatorTest {

    // 1
    @ParameterizedTest
    @CsvSource({
        "95, A",
        "90, A",
        "89.99, B",
        "80, B",
        "70, C",
        "60, D",
        "59.99, F",
        "0, F",
        "100, A"
    })
    @DisplayName("Үсгэн дүнгийн ердийн болон хязгаарын утгууд зөв байх")
    void letterGradeBoundaries(double score, String expected) {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        String result = calc.letterGrade(score);

        // Assert
        assertEquals(expected, result);
    }

    // 2
    @Test
    @DisplayName("Сөрөг оноо exception шидэх ёстой")
    void negativeScoreThrowsException() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        Executable action = () -> calc.letterGrade(-1);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    // 3
    @Test
    @DisplayName("100-аас их оноо exception шидэх ёстой")
    void scoreAbove100ThrowsException() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        Executable action = () -> calc.letterGrade(101);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    // 4
    @Test
    @DisplayName("Дээд хязгаарын нийлбэр 100 байх")
    void maximumTotalScoreIs100() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        double result = calc.totalScore(10, 40, 10, 10, 30);

        // Assert
        assertEquals(100.0, result);
    }

    // 5
    @Test
    @DisplayName("Ердийн онооны нийлбэр зөв гарах")
    void normalTotalScoreIsCorrect() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        double result = calc.totalScore(8, 30, 8, 9, 25);

        // Assert
        assertEquals(80.0, result);
    }

    // 6
    @Test
    @DisplayName("Сөрөг ирц exception шидэх ёстой")
    void negativeAttendanceThrowsException() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        Executable action =
                () -> calc.totalScore(-5, 30, 8, 8, 20);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    // 7
    @Test
    @DisplayName("Лабын оноо 40-өөс их бол exception шидэх ёстой")
    void labAboveMaximumThrowsException() {
        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        Executable action =
                () -> calc.totalScore(10, 41, 10, 10, 30);

        // Assert
        assertThrows(IllegalArgumentException.class, action);
    }

    // 8
    @ParameterizedTest
    @CsvSource({
        "10, 40, 10, 10, 30, 100",
        "5, 20, 5, 5, 15, 50",
        "0, 0, 0, 0, 0, 0"
    })
    @DisplayName("totalScore олон утгаар зөв нийлбэр гаргах")
    void totalScoreParameterized(
            double att,
            double lab,
            double quiz1,
            double quiz2,
            double exam,
            double expected) {

        // Arrange
        GradeCalculator calc = new GradeCalculator();

        // Act
        double result =
                calc.totalScore(att, lab, quiz1, quiz2, exam);

        // Assert
        assertEquals(expected, result);
    }
}
