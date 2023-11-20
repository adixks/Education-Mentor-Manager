package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LessonMapper;
import pl.szlify.codingapi.model.dto.LessonDto;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;

    private Faker faker;

    @BeforeEach
    public void setup() {
        faker = new Faker();
    }

    @Test
    public void getAllLessonsTest() {
        // Given
        LessonEntity lessonEntity = new LessonEntity().setId(faker.number().randomNumber());
        LessonDto lessonDto = new LessonDto();

        Page<LessonEntity> pagedResponse = new PageImpl<>(Collections.singletonList(lessonEntity));
        Pageable pageable = PageRequest.of(0, 5);
        when(lessonRepository.findAll(pageable)).thenReturn(pagedResponse);
        when(lessonMapper.toDto(lessonEntity)).thenReturn(lessonDto);

        // When
        Page<LessonDto> result = lessonService.getAllLessons(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(lessonDto, result.getContent().get(0));
    }

    @Test
    public void getLessonTest() {
        // Given
        Long id = faker.number().randomNumber();
        LessonEntity lessonEntity = new LessonEntity().setId(id);
        LessonDto lessonDto = new LessonDto();
        givenLessonEntityInRepository(id, lessonEntity, lessonDto);

        // When
        LessonDto result = lessonService.getLesson(id);

        // Then
        assertLessonDtoResult(lessonDto, result);
    }

    @Test
    public void getLessonTest_shouldThrowNoLessonsException() {
        // Given
        Long nonExistentLessonId = faker.number().randomNumber();
        when(lessonRepository.findById(nonExistentLessonId)).thenReturn(Optional.empty());

        // When
        assertThrows(NoLessonsException.class, () -> lessonService.getLesson(nonExistentLessonId));

        // Then
        verify(lessonRepository).findById(nonExistentLessonId);
        verifyNoInteractions(lessonMapper);
    }

    @Test
    public void addLessonTest() {
        LessonDto lessonDto = generateRandomLessonDto();
        TeacherEntity teacherEntity = new TeacherEntity().setId(lessonDto.getTeacherId());
        StudentEntity studentEntity = new StudentEntity().setTeacher(teacherEntity);
        LessonEntity lessonEntity = new LessonEntity()
                .setTeacher(teacherEntity)
                .setStudent(studentEntity);
        LocalDateTime start = lessonDto.getDate();
        LocalDateTime end = start.plusMinutes(60).plusMinutes(15);

        when(teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndDeletedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));
        when(lessonRepository.existsByTeacherIdAndDateBetween(lessonDto.getTeacherId(), start.minusMinutes(75), end.plusMinutes(75))).thenReturn(false);
        when(lessonMapper.toEntity(lessonDto)).thenReturn(lessonEntity);
        when(lessonRepository.save(lessonEntity)).thenReturn(lessonEntity);
        when(lessonMapper.toDto(lessonEntity)).thenReturn(lessonDto);

        LessonDto result = lessonService.addLesson(lessonDto);

        verify(teacherRepository, times(1)).findByIdAndDeletedFalse(lessonDto.getTeacherId());
        verify(studentRepository, times(1)).findByIdAndDeletedFalse(lessonDto.getStudentId());
        verify(lessonRepository, times(1)).existsByTeacherIdAndDateBetween(lessonDto.getTeacherId(), start.minusMinutes(75), end.plusMinutes(75));
        verify(lessonRepository, times(1)).save(lessonEntity);
        assertEquals(lessonDto, result);
    }

    @Test
    public void addLessonTest_shouldThrowLackofTeacherException() {
        // Given
        LessonDto lessonDto = generateRandomLessonDto();
        when(teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())).thenReturn(Optional.empty());

        // When
        assertThrows(LackOfTeacherException.class, () -> lessonService.addLesson(lessonDto));

        // Then
        verify(teacherRepository).findByIdAndDeletedFalse(lessonDto.getTeacherId());
        verifyNoInteractions(studentRepository, lessonRepository, lessonMapper);
    }

    @Test
    public void addLessonTest_shouldThrowMissingStudentException() {
        // Given
        LessonDto lessonDto = generateRandomLessonDto();
        TeacherEntity teacherEntity = new TeacherEntity();
        when(teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndDeletedFalse(lessonDto.getStudentId())).thenReturn(Optional.empty());

        // When
        assertThrows(MissingStudentException.class, () -> lessonService.addLesson(lessonDto));

        // Then
        verify(teacherRepository).findByIdAndDeletedFalse(lessonDto.getTeacherId());
        verify(studentRepository).findByIdAndDeletedFalse(lessonDto.getStudentId());
        verifyNoInteractions(lessonRepository, lessonMapper);
    }

    @Test
    public void addLessonTest_shouldThrowNotYourTeacherException() {
        // Given
        LessonDto lessonDto = generateRandomLessonDto();
        TeacherEntity teacherEntity = new TeacherEntity().setId(2L);
        StudentEntity studentEntity = new StudentEntity().setTeacher(teacherEntity);
        when(teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndDeletedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));

        // When
        Exception exception = assertThrows(NotYourTeacherException.class, () -> {
            lessonService.addLesson(lessonDto);
        });

        // Then
        String expectedMessage = "It's not your teacher";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void updateEntireLessonTest_shouldThrowBusyTermLessonException() {
        // Given
        Long id = faker.number().randomNumber();
        LessonDto lessonDto = new LessonDto()
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(lessonDto.getTeacherId());

        StudentEntity studentEntity = new StudentEntity()
                .setId(lessonDto.getStudentId())
                .setTeacher(teacherEntity);

        LessonEntity conflictingLesson = new LessonEntity()
                .setId(faker.number().randomNumber())
                .setDate(lessonDto.getDate())
                .setTeacher(teacherEntity);

        when(teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndDeletedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));
        when(lessonRepository.findByTeacherIdAndDateBetween(any(), any(), any())).thenReturn(Arrays.asList(conflictingLesson));

        // When
        assertThrows(BusyTermLessonException.class, () -> lessonService.updateEntireLesson(id, lessonDto));

        // Then
        verify(teacherRepository, times(1)).findByIdAndDeletedFalse(lessonDto.getTeacherId());
        verify(studentRepository, times(1)).findByIdAndDeletedFalse(lessonDto.getStudentId());
        verify(lessonRepository, times(1)).findByTeacherIdAndDateBetween(any(), any(), any());
        verify(lessonRepository, never()).findById(any());
        verify(lessonRepository, never()).save(any());
    }


    @Test
    public void updateLessonDateTest() {
        // Given
        Long id = faker.number().randomNumber();
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);

        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setDate(LocalDateTime.now())
                .setTeacher(new TeacherEntity());

        LessonDto updatedLessonDto = new LessonDto()
                .setDate(newDateTime);

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));
        when(lessonRepository.findByTeacherIdAndDateBetween(any(), any(), any())).thenReturn(Collections.emptyList());
        when(lessonMapper.toDto(any())).thenReturn(updatedLessonDto);

        // When
        LessonDto result = lessonService.updateLessonDate(id, newDateTime);

        // Then
        assertNotNull(result);
        assertEquals(updatedLessonDto.getDate(), result.getDate());
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, times(1)).findByTeacherIdAndDateBetween(any(), any(), any());
        verify(lessonRepository, times(1)).save(any());
    }

    @Test
    public void updateLessonDateTest_shouldThrowNoLessonsException() {
        // Given
        Long id = faker.number().randomNumber();
        LocalDateTime newDateTime = LocalDateTime.now();

        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(NoLessonsException.class, () -> lessonService.updateLessonDate(id, newDateTime));

        // Then
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLessonDateTest_shouldThrowBusyTermLessonException() {
        // Given
        Long id = faker.number().randomNumber();
        LocalDateTime newDateTime = LocalDateTime.now();

        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setDate(LocalDateTime.now())
                .setTeacher(new TeacherEntity());

        LessonEntity conflictingLesson = new LessonEntity()
                .setId(faker.number().randomNumber())
                .setDate(newDateTime)
                .setTeacher(new TeacherEntity());

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));
        when(lessonRepository.findByTeacherIdAndDateBetween(any(), any(), any())).thenReturn(Arrays.asList(conflictingLesson));

        // When
        assertThrows(BusyTermLessonException.class, () -> lessonService.updateLessonDate(id, newDateTime));

        // Then
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, times(1)).findByTeacherIdAndDateBetween(any(), any(), any());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLessonDateTest_ThrowsNoLessonsException() {
        // Given
        LocalDateTime localDateTime = LocalDateTime.now();
        when(lessonRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NoLessonsException.class, () -> lessonService.updateLessonDate(faker.number().randomNumber(), localDateTime));
    }

    @Test
    public void deleteLessonTest() {
        // Given
        Long id = faker.number().randomNumber();
        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setDate(LocalDateTime.now().plusDays(1));
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));

        // When
        lessonService.deleteLesson(id);

        // Then
        verify(lessonRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteLessonTest_shouldThrowNoLessonsException() {
        // Given
        Long id = faker.number().randomNumber();
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        // When - Then
        assertThrows(NoLessonsException.class, () -> lessonService.deleteLesson(id));
    }

    @Test
    public void deleteLessonTest_shouldThrowLessonLearnedException() {
        // Given
        Long id = faker.number().randomNumber();
        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setDate(LocalDateTime.now().minusDays(1));
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));

        // When - Then
        assertThrows(LessonLearnedException.class, () -> lessonService.deleteLesson(id));
    }


    private void givenLessonEntityInRepository(Long id, LessonEntity lessonEntity, LessonDto lessonDto) {
        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));
        when(lessonMapper.toDto(lessonEntity)).thenReturn(lessonDto);
    }

    private void assertLessonDtoResult(LessonDto expected, LessonDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getTeacherId(), actual.getTeacherId());
        assertEquals(expected.getStudentId(), actual.getStudentId());
        assertEquals(expected.getDate(), actual.getDate());
    }

    private LessonDto generateRandomLessonDto() {
        return new LessonDto()
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 365)));
    }
}
