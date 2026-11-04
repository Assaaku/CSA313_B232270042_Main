package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseRegistrationServiceTest {

    @Mock
    StudentRepository repository;

    @Mock
    EmailSender emailSender;

    CourseRegistrationService service;

    @BeforeEach
    void setUp() {
        service = new CourseRegistrationService(repository, emailSender);
    }

    @Test
    void идэвхтэйОюутан_шаардлагаХангасанБол_OKБуцаажХадгалжИмэйлИлгээнэ() {
        Student student = new Student(
                "S001",
                "student@example.com",
                true,
                List.of("CS101"));

        when(repository.findStudent("S001")).thenReturn(student);
        when(repository.courseExists("CS201")).thenReturn(true);
        when(repository.prerequisites("CS201")).thenReturn(List.of("CS101"));

        String result = service.register("S001", "CS201");

        assertEquals("OK", result);
        verify(repository).saveRegistration("S001", "CS201");
        verify(emailSender).send(
                eq("student@example.com"),
                eq("Бүртгэл баталгаажлаа"),
                contains("CS201"));
    }

    @Test
    void оюутанОлдохгүйБол_ERROR_NO_STUDENTБуцаажХадгалахгүйИмэйлИлгээхгүй() {
        when(repository.findStudent("UNKNOWN")).thenReturn(null);

        String result = service.register("UNKNOWN", "CS201");

        assertEquals("ERROR_NO_STUDENT", result);
        verify(repository, never()).saveRegistration(anyString(), anyString());
        verify(emailSender, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void идэвхгүйОюутанБол_ERROR_INACTIVE_STUDENTБуцаажИмэйлтэйХарьцахгүй() {
        Student student = new Student(
                "S002",
                "inactive@example.com",
                false,
                List.of());

        when(repository.findStudent("S002")).thenReturn(student);

        String result = service.register("S002", "CS201");

        assertEquals("ERROR_INACTIVE_STUDENT", result);
        verifyNoInteractions(emailSender);
        verify(repository, never()).saveRegistration(anyString(), anyString());
    }

    @Test
    void өмнөхХичээлДутууБол_ERROR_PREREQUISITESБуцаажХадгалахгүй() {
        Student student = new Student(
                "S003",
                "student@example.com",
                true,
                List.of("CS100"));

        when(repository.findStudent("S003")).thenReturn(student);
        when(repository.courseExists("CS301")).thenReturn(true);
        when(repository.prerequisites("CS301"))
                .thenReturn(List.of("CS101", "CS201"));

        String result = service.register("S003", "CS301");

        assertEquals("ERROR_PREREQUISITES", result);
        verify(repository, never()).saveRegistration(anyString(), anyString());
        verify(emailSender, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void имэйлExceptionШидсэнЧ_OKБуцаажБүртгэлийгХадгална() {
        Student student = new Student(
                "S004",
                "student@example.com",
                true,
                List.of());

        when(repository.findStudent("S004")).thenReturn(student);
        when(repository.courseExists("CS101")).thenReturn(true);
        when(repository.prerequisites("CS101")).thenReturn(List.of());

        doThrow(new RuntimeException("SMTP error"))
                .when(emailSender)
                .send(anyString(), anyString(), anyString());

        String result = service.register("S004", "CS101");

        assertEquals("OK", result);
        verify(repository).saveRegistration("S004", "CS101");
        verify(emailSender).send(
                eq("student@example.com"),
                eq("Бүртгэл баталгаажлаа"),
                contains("CS101"));
    }

    @Test
    void nullОролтБол_IllegalArgumentExceptionШидэжХамаарлуудтайХарьцахгүй() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.register(null, "CS101"));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.register("S001", null));

        verifyNoInteractions(repository, emailSender);
    }

    @Test
    void амжилттайБүртгэлд_ИмэйлийнBodyХичээлийнКодАгуулна() {
        Student student = new Student(
                "S005",
                "capture@example.com",
                true,
                List.of());

        when(repository.findStudent("S005")).thenReturn(student);
        when(repository.courseExists("CS777")).thenReturn(true);
        when(repository.prerequisites("CS777")).thenReturn(List.of());

        service.register("S005", "CS777");

        ArgumentCaptor<String> bodyCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(emailSender).send(
                eq("capture@example.com"),
                eq("Бүртгэл баталгаажлаа"),
                bodyCaptor.capture());

        assertTrue(bodyCaptor.getValue().contains("CS777"));
    }

    @ParameterizedTest
    @CsvSource({
            "NO_STUDENT, ERROR_NO_STUDENT",
            "INACTIVE, ERROR_INACTIVE_STUDENT",
            "NO_COURSE, ERROR_NO_COURSE"
    })
    void алдааныНөхцлүүдэд_ЗөвАлдааныКодБуцаана(
            String scenario,
            String expected) {

        Student activeStudent = new Student(
                "S100",
                "active@example.com",
                true,
                List.of());

        Student inactiveStudent = new Student(
                "S100",
                "inactive@example.com",
                false,
                List.of());

        if ("NO_STUDENT".equals(scenario)) {
            lenient().when(repository.findStudent("S100"))
                    .thenReturn(null);
        } else if ("INACTIVE".equals(scenario)) {
            lenient().when(repository.findStudent("S100"))
                    .thenReturn(inactiveStudent);
        } else if ("NO_COURSE".equals(scenario)) {
            lenient().when(repository.findStudent("S100"))
                    .thenReturn(activeStudent);
            lenient().when(repository.courseExists("CS999"))
                    .thenReturn(false);
        }

        String result = service.register("S100", "CS999");

        assertEquals(expected, result);
        verify(repository, never())
                .saveRegistration(anyString(), anyString());
        verify(emailSender, never())
                .send(anyString(), anyString(), anyString());
    }
}
