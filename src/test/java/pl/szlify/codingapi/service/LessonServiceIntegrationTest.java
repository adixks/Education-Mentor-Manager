package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LessonMapper;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class LessonServiceIntegrationTest {

    @Autowired
    private LessonService lessonService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private LessonMapper lessonMapper;

    private static final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllLessonsTest() {
        // Given
        LessonEntity lessonEntity1 = new LessonEntity()
                .setId(faker.number().randomNumber());

        LessonEntity lessonEntity2 = new LessonEntity()
                .setId(faker.number().randomNumber());

        List<LessonEntity> lessonEntities = Arrays.asList(lessonEntity1, lessonEntity2);

        when(lessonRepository.findAll()).thenReturn(lessonEntities);
        when(lessonMapper.fromEntityToDto(lessonEntity1)).thenReturn(new LessonDto());
        when(lessonMapper.fromEntityToDto(lessonEntity2)).thenReturn(new LessonDto());

        // When
        List<LessonDto> result = lessonService.getAllLessons();

        // Then
        assertNotNull(result);
        assertEquals(lessonEntities.size(), result.size());
        verify(lessonMapper, times(lessonEntities.size())).fromEntityToDto(any());
    }

    @Test
    public void getLessonTest() {
        // Given
        Long lessonId = faker.number().randomNumber();

        LessonEntity lessonEntity = new LessonEntity()
                .setId(lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonEntity));
        when(lessonMapper.fromEntityToDto(lessonEntity)).thenReturn(new LessonDto());

        // When
        LessonDto result = lessonService.getLesson(lessonId);

        // Then
        assertNotNull(result);
        verify(lessonMapper, times(1)).fromEntityToDto(any());
    }

    @Test
    public void getLessonTest_shouldThrowsNoLessonsException() {
        // Given
        Long lessonId = faker.number().randomNumber();

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // When - Then
        NoLessonsException exception = assertThrows(NoLessonsException.class, () -> {
            lessonService.getLesson(lessonId);
        });

        assertNotNull(exception);
        verify(lessonMapper, never()).fromEntityToDto(any());
    }

    @Test
    public void addLessonTest() {
        // Given
        LessonDto lessonDto = new LessonDto()
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(lessonDto.getTeacherId());

        StudentEntity studentEntity = new StudentEntity()
                .setId(lessonDto.getStudentId())
                .setTeacherEntity(teacherEntity);

        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate()))
                .thenReturn(Optional.empty());
        when(lessonMapper.fromDtoToEntity(lessonDto)).thenReturn(new LessonEntity());
        when(lessonMapper.fromEntityToDto(any(LessonEntity.class))).thenAnswer(invocation -> {
            LessonEntity entity = invocation.getArgument(0);
            LessonDto dto = new LessonDto();
            dto.setId(entity.getId());
            return dto;
        });
        when(lessonRepository.save(any(LessonEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        LessonDto result = lessonService.addLesson(lessonDto);

        // Then
        assertNotNull(result);
        verify(lessonRepository, times(1)).save(any(LessonEntity.class));
    }

    @Test
    public void addLessonTest_shouldThrowsLackOfTeacherException() {
        // Given
        LessonDto lessonDto = new LessonDto()
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now());

        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            lessonService.addLesson(lessonDto);
        });

        assertNotNull(exception);
        verify(lessonRepository, never()).save(any(LessonEntity.class));
    }

    @Test
    public void addLessonTest_shouldThrowsMissingStudentException() {
        // Given
        LessonDto lessonDto = new LessonDto()
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(lessonDto.getTeacherId());

        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(lessonDto.getStudentId())).thenReturn(Optional.empty());

        // When - Then
        MissingStudentException exception = assertThrows(MissingStudentException.class, () -> {
            lessonService.addLesson(lessonDto);
        });

        assertNotNull(exception);
        verify(lessonRepository, never()).save(any(LessonEntity.class));
    }

    @Test
    public void addLessonTest_shouldThrowsBusyTermLessonException() {
        // Given
        LessonDto lessonDto = new LessonDto()
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(lessonDto.getTeacherId());

        StudentEntity studentEntity = new StudentEntity()
                .setId(lessonDto.getStudentId())
                .setTeacherEntity(teacherEntity);

        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate()))
                .thenReturn(Optional.of(new LessonEntity()));

        // When - Then
        BusyTermLessonException exception = assertThrows(BusyTermLessonException.class, () -> {
            lessonService.addLesson(lessonDto);
        });

        assertNotNull(exception);
        verify(lessonRepository, never()).save(any(LessonEntity.class));
    }

    @Test
    public void updateLessonDateTest() {
        // Given
        Long lessonId = faker.number().randomNumber();
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);

        LessonEntity lessonEntity = new LessonEntity()
                .setId(lessonId)
                .setTeacherEntity(new TeacherEntity())
                .setDate(LocalDateTime.now());

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(anyLong(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());
        when(lessonMapper.fromEntityToDto(any(LessonEntity.class))).thenAnswer(invocation -> {
            LessonEntity entity = invocation.getArgument(0);
            LessonDto dto = new LessonDto();
            dto.setId(entity.getId());
            return dto;
        });
        when(lessonRepository.save(any(LessonEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        LessonDto result = lessonService.updateLessonDate(lessonId, newDateTime);

        // Then
        assertNotNull(result);
        assertEquals(newDateTime, lessonEntity.getDate());
        verify(lessonRepository, times(1)).save(any(LessonEntity.class));
    }

    @Test
    public void updateLessonDateTest_should_ThrowsNoLessonsException() {
        // Given
        Long lessonId = faker.number().randomNumber();
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // When - Then
        NoLessonsException exception = assertThrows(NoLessonsException.class, () -> {
            lessonService.updateLessonDate(lessonId, newDateTime);
        });

        assertNotNull(exception);
        verify(lessonRepository, never()).save(any(LessonEntity.class));
    }

    @Test
    public void deleteLessonTest() {
        // Given
        Long lessonId = faker.number().randomNumber();

        LessonEntity lessonEntity = new LessonEntity()
                .setId(lessonId)
                .setDate(LocalDateTime.now().plusDays(1));

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonEntity));

        // When
        lessonService.deleteLesson(lessonId);

        // Then
        verify(lessonRepository, times(1)).deleteById(lessonId);
    }

    @Test
    public void deleteLessonTest_shouldThrowsNoLessonsException() {
        // Given
        Long lessonId = faker.number().randomNumber();

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // When - Then
        NoLessonsException exception = assertThrows(NoLessonsException.class, () -> {
            lessonService.deleteLesson(lessonId);
        });

        assertNotNull(exception);
        verify(lessonRepository, never()).deleteById(anyLong());
    }

    @Test
    public void deleteLessonTest_shouldThrowsLessonLearnedException() {
        // Given
        Long lessonId = faker.number().randomNumber();

        LessonEntity lessonEntity = new LessonEntity()
                .setId(lessonId)
                .setDate(LocalDateTime.now().minusDays(1));

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lessonEntity));

        // When - Then
        LessonLearnedException exception = assertThrows(LessonLearnedException.class, () -> {
            lessonService.deleteLesson(lessonId);
        });

        assertNotNull(exception);
        verify(lessonRepository, never()).deleteById(anyLong());
    }
}
