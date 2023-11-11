package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.StudentDto;
import pl.szlify.codingapi.model.StudentBasicInfoDto;
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
        List<StudentBasicInfoDto> studentsList = Arrays.asList(new StudentBasicInfoDto(), new StudentBasicInfoDto());
        when(studentService.getStudentsList()).thenReturn(studentsList);

        // When
        List<StudentBasicInfoDto> result = studentController.getStudentsList();

        // Then
        assertEquals(studentsList, result);
    }

    @Test
    void getStudent_shouldReturnListOfStudentDto() {
        // Given
        Long studentId = 1L;
        StudentDto studentDto = new StudentDto();
        when(studentService.getStudent(studentId)).thenReturn(studentDto);

        // When
        StudentDto result = studentController.getStudent(studentId);

        // Then
        assertEquals(studentDto, result);
    }

    @Test
    void addStudent_shouldReturnStudentBasicInfoDto() {
        // Given
        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto();
        when(studentService.addStudent(studentBasicInfoDto)).thenReturn(studentBasicInfoDto);

        // When
        StudentBasicInfoDto result = studentController.addStudent(studentBasicInfoDto);

        // Then
        assertEquals(studentBasicInfoDto, result);
    }

    @Test
    void updateEntireStudent_shouldReturnStudentDto() {
        // Given
        Long studentId = 1L;
        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto();
        StudentDto studentDto = new StudentDto();
        when(studentService.updateEntireStudent(studentId, studentBasicInfoDto)).thenReturn(studentDto);

        // When
        StudentDto result = studentController.updateEntireStudent(studentId, studentBasicInfoDto);

        // Then
        assertEquals(studentDto, result);
    }

    @Test
    void updateStudentTeacher_shouldReturnStudentBasicInfoDto() {
        // Given
        Long studentId = 1L;
        Long newTeacherId = 2L;
        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto();
        when(studentService.updateStudentTeacher(studentId, newTeacherId)).thenReturn(studentBasicInfoDto);

        // When
        StudentBasicInfoDto result = studentController.updateStudentTeacher(studentId, newTeacherId);

        // Then
        assertEquals(studentBasicInfoDto, result);
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
