package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.dto.LessonDateDto;
import pl.szlify.codingapi.model.dto.LessonDto;
import pl.szlify.codingapi.service.LessonService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @Test
    public void getAllLessons_shouldReturnListOfLessonsDto() {
        // Given
        List<LessonDto> lessonsList = Arrays.asList(new LessonDto(), new LessonDto());
        Page<LessonDto> page = new PageImpl<>(lessonsList);
        Pageable pageable = PageRequest.of(0, 5);
        when(lessonService.getList(pageable)).thenReturn(page);

        // When
        Page<LessonDto> result = lessonController.getAllLessons(pageable);

        // Then
        assertEquals(page.getContent(), result.getContent());
    }

    @Test
    public void getLesson_shouldReturnLessonDto() {
        // Given
        Long id = 1L;
        LessonDto lesson = new LessonDto();
        when(lessonService.getLesson(id)).thenReturn(lesson);

        // When
        LessonDto result = lessonController.getLesson(id);

        // Then
        assertEquals(lesson, result);
    }

    @Test
    public void addLesson_shouldReturnLessonDto() {
        // Given
        LessonDto lesson = new LessonDto();
        when(lessonService.addLesson(lesson)).thenReturn(lesson);

        // When
        LessonDto result = lessonController.addLesson(lesson);

        // Then
        assertEquals(lesson, result);
    }

    @Test
    void updateEntireLesson_shouldReturnUpdatedLessonDto() {
        // Given
        Long lessonId = 1L;
        LessonDto lessonDto = new LessonDto();
        LessonDto updatedLessonDto = new LessonDto();
        when(lessonService.updateEntireLesson(lessonId, lessonDto)).thenReturn(updatedLessonDto);

        // When
        LessonDto result = lessonController.updateEntireLesson(lessonId, lessonDto);

        // Then
        assertEquals(updatedLessonDto, result);
        verify(lessonService, times(1)).updateEntireLesson(lessonId, lessonDto);
    }

    @Test
    public void updateLessonDate_shouldReturnUpdatedLessonDto() {
        // Given
        Long id = 1L;
        LessonDateDto localDateTime = new LessonDateDto().setDate(LocalDateTime.now());
        LessonDto lesson = new LessonDto();
        when(lessonService.updateLessonDate(id, localDateTime.getDate())).thenReturn(lesson);

        // When
        LessonDto result = lessonController.updateLessonDate(id, localDateTime);

        // Then
        assertEquals(lesson, result);
    }

    @Test
    public void deleteLesson_shouldReturn200() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> responseEntity = lessonController.deleteLesson(id);

        // When
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(lessonService, times(1)).deleteLesson(id);
    }
}
