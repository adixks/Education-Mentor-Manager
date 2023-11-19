package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.dto.StudentFullDto;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.service.StudentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void getStudentsList_shouldReturnListOfStudentBasicInfoDto() {
        // Given
        List<StudentShortDto> studentsList = Arrays.asList(new StudentShortDto(), new StudentShortDto());
        when(studentService.getStudentsList()).thenReturn(studentsList);

        // When
        List<StudentShortDto> result = studentController.getStudentsList();

        // Then
        assertEquals(studentsList, result);
    }

    @Test
    void getStudent_shouldReturnListOfStudentDto() {
        // Given
        Long studentId = 1L;
        StudentFullDto studentFullDto = new StudentFullDto();
        when(studentService.getStudent(studentId)).thenReturn(studentFullDto);

        // When
        StudentFullDto result = studentController.getStudent(studentId);

        // Then
        assertEquals(studentFullDto, result);
    }

    @Test
    void addStudent_shouldReturnStudentBasicInfoDto() {
        // Given
        StudentShortDto studentShortDto = new StudentShortDto();
        when(studentService.addStudent(studentShortDto)).thenReturn(studentShortDto);

        // When
        StudentShortDto result = studentController.addStudent(studentShortDto);

        // Then
        assertEquals(studentShortDto, result);
    }

    @Test
    void updateEntireStudent_shouldReturnStudentDto() {
        // Given
        Long studentId = 1L;
        StudentShortDto studentShortDto = new StudentShortDto();
        StudentFullDto studentFullDto = new StudentFullDto();
        when(studentService.updateEntireStudent(studentId, studentShortDto)).thenReturn(studentFullDto);

        // When
        StudentFullDto result = studentController.updateEntireStudent(studentId, studentShortDto);

        // Then
        assertEquals(studentFullDto, result);
    }

    @Test
    void updateStudentTeacher_shouldReturnStudentBasicInfoDto() {
        // Given
        Long studentId = 1L;
        Long newTeacherId = 2L;
        StudentShortDto studentShortDto = new StudentShortDto();
        when(studentService.updateStudentTeacher(studentId, newTeacherId)).thenReturn(studentShortDto);

        // When
        StudentShortDto result = studentController.updateStudentTeacher(studentId, newTeacherId);

        // Then
        assertEquals(studentShortDto, result);
    }

    @Test
    void deleteStudent_shouldReturn200() {
        // Given
        Long studentId = 1L;

        // When
        ResponseEntity<Void> result = studentController.deleteStudent(studentId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(studentService, times(1)).deleteStudent(studentId);
    }
}
