package mn.edu.must.sqat;

public class GradeCalculator {

    public String letterGrade(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }

        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";

        return "F";
    }

    public double totalScore(
            double att,
            double lab,
            double quiz1,
            double quiz2,
            double exam) {

        if (att < 0 || att > 10) {
            throw new IllegalArgumentException("Attendance must be between 0 and 10");
        }

        if (lab < 0 || lab > 40) {
            throw new IllegalArgumentException("Lab must be between 0 and 40");
        }

        if (quiz1 < 0 || quiz1 > 10) {
            throw new IllegalArgumentException("Quiz1 must be between 0 and 10");
        }

        if (quiz2 < 0 || quiz2 > 10) {
            throw new IllegalArgumentException("Quiz2 must be between 0 and 10");
        }

        if (exam < 0 || exam > 30) {
            throw new IllegalArgumentException("Exam must be between 0 and 30");
        }

        return att + lab + quiz1 + quiz2 + exam;
    }
}
