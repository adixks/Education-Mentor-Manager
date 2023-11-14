package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.model.TeacherLanguagesDto;
import pl.szlify.codingapi.service.TeacherService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    void getTeachersList_shouldReturnListOfTeacherBasicInfoDto() {
        // Given
        when(teacherService.getTeachersList()).thenReturn(Arrays.asList(new TeacherBasicInfoDto(), new TeacherBasicInfoDto()));

        // When
        List<TeacherBasicInfoDto> result = teacherController.getTeachersList();

        // Then
        assertEquals(2, result.size());
        verify(teacherService, times(1)).getTeachersList();
    }

    @Test
    void getTeacher_shouldReturnStudentDto() {
        // Given
        Long id = 1L;
        when(teacherService.getTeacher(id)).thenReturn(new TeacherDto());

        // When
        TeacherDto result = teacherController.getTeacher(id);

        // Then
        assertEquals(TeacherDto.class, result.getClass());
        verify(teacherService, times(1)).getTeacher(id);
    }

    @Test
    void addTeacher_shouldReturnTeacherDto() {
        // Given
        TeacherBasicInfoDto teacherDto = new TeacherBasicInfoDto();
        when(teacherService.addTeacher(teacherDto)).thenReturn(new TeacherDto());

        // When
        TeacherDto result = teacherController.addTeacher(teacherDto);

        // Then
        assertEquals(TeacherDto.class, result.getClass());
        verify(teacherService, times(1)).addTeacher(teacherDto);
    }

    @Test
    void updateEntireTeacher_() {
        // Given
        Long id = 1L;
        TeacherBasicInfoDto teacherDto = new TeacherBasicInfoDto();
        when(teacherService.updateEntireTeacher(id, teacherDto)).thenReturn(teacherDto);

        // When
        TeacherBasicInfoDto result = teacherController.updateEntireTeacher(id, teacherDto);

        // Then
        assertEquals(TeacherBasicInfoDto.class, result.getClass());
        verify(teacherService, times(1)).updateEntireTeacher(id, teacherDto);
    }

    @Test
    void updateTeacherLanguagesList_shouldReturnTeacherBasicInfoDto() {
        // Given
        Long id = 1L;
        TeacherLanguagesDto languagesList = new TeacherLanguagesDto().setLanguagesList(Arrays.asList("jezyk1", "jezyk2"));
        TeacherBasicInfoDto teacherDto = new TeacherBasicInfoDto();
        when(teacherService.updateTeacherLanguagesList(id, languagesList.getLanguagesList())).thenReturn(teacherDto);

        // When
        TeacherBasicInfoDto result = teacherController.updateTeacherLanguagesList(id, languagesList);

        // Then
        assertEquals(TeacherBasicInfoDto.class, result.getClass());
        verify(teacherService, times(1)).updateTeacherLanguagesList(id, languagesList.getLanguagesList());
    }

    @Test
    void deleteTeacher_shouldReturn200() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> result = teacherController.deleteTeacher(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(teacherService, times(1)).deleteTeacher(id);
    }
}
