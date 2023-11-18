package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LessonMapper;
import pl.szlify.codingapi.model.LessonDto;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        LessonDto lessonDto = new LessonDto().setId(lessonEntity.getId());
        when(lessonRepository.findAll()).thenReturn(Collections.singletonList(lessonEntity));
        when(lessonMapper.fromEntityToDto(lessonEntity)).thenReturn(lessonDto);

        // When
        List<LessonDto> result = lessonService.getAllLessons();

        // Then
        assertEquals(1, result.size());
        assertEquals(lessonDto, result.get(0));
    }

    @Test
    public void getLessonTest() {
        // Given
        Long id = faker.number().randomNumber();
        LessonEntity lessonEntity = new LessonEntity().setId(id);
        LessonDto lessonDto = new LessonDto().setId(lessonEntity.getId());
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
        // Given
        LessonDto lessonDto = generateRandomLessonDto();

        TeacherEntity teacherEntity = new TeacherEntity().setId(lessonDto.getTeacherId());
        StudentEntity studentEntity = new StudentEntity().setTeacherEntity(teacherEntity);

        LessonEntity lessonEntity = new LessonEntity()
                .setTeacherEntity(teacherEntity)
                .setStudentEntity(studentEntity);

        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate())).thenReturn(Optional.empty());
        when(lessonMapper.fromDtoToEntity(lessonDto)).thenReturn(lessonEntity);
        when(lessonMapper.fromEntityToDto(lessonEntity)).thenReturn(lessonDto);

        // When
        LessonDto result = lessonService.addLesson(lessonDto);

        // Then
        verify(teacherRepository, times(1)).findByIdAndRemovedFalse(lessonDto.getTeacherId());
        verify(studentRepository, times(1)).findByIdAndRemovedFalse(lessonDto.getStudentId());
        verify(lessonRepository, times(1)).findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate());
        verify(lessonMapper, times(1)).fromDtoToEntity(lessonDto);
        verify(lessonMapper, times(1)).fromEntityToDto(lessonEntity);
        assertEquals(lessonDto, result);
    }

    @Test
    public void addLessonTest_shouldThrowLackofTeacherException() {
        // Given
        LessonDto lessonDto = generateRandomLessonDto();
        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.empty());

        // When
        assertThrows(LackOfTeacherException.class, () -> lessonService.addLesson(lessonDto));

        // Then
        verify(teacherRepository).findByIdAndRemovedFalse(lessonDto.getTeacherId());
        verifyNoInteractions(studentRepository, lessonRepository, lessonMapper);
    }

    @Test
    public void addLessonTest_shouldThrowMissingStudentException() {
        // Given
        LessonDto lessonDto = generateRandomLessonDto();
        TeacherEntity teacherEntity = new TeacherEntity();
        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(lessonDto.getStudentId())).thenReturn(Optional.empty());

        // When
        assertThrows(MissingStudentException.class, () -> lessonService.addLesson(lessonDto));

        // Then
        verify(teacherRepository).findByIdAndRemovedFalse(lessonDto.getTeacherId());
        verify(studentRepository).findByIdAndRemovedFalse(lessonDto.getStudentId());
        verifyNoInteractions(lessonRepository, lessonMapper);
    }

    @Test
    public void addLessonTest_shouldThrowNotYourTeacherException() {
        // Given
        LessonDto lessonDto = generateRandomLessonDto();
        TeacherEntity teacherEntity = new TeacherEntity().setId(2L);
        StudentEntity studentEntity = new StudentEntity().setTeacherEntity(teacherEntity);
        when(teacherRepository.findByIdAndRemovedFalse(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));

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
        LessonDto updatedLessonDto = generateRandomLessonDto();

        TeacherEntity mockTeacherEntity = new TeacherEntity()
                .setId(updatedLessonDto.getTeacherId());

        StudentEntity mockStudentEntity = new StudentEntity()
                .setId(updatedLessonDto.getStudentId())
                .setTeacherEntity(mockTeacherEntity);

        when(teacherRepository.findByIdAndRemovedFalse(updatedLessonDto.getTeacherId())).thenReturn(Optional.of(mockTeacherEntity));
        when(studentRepository.findByIdAndRemovedFalse(updatedLessonDto.getStudentId())).thenReturn(Optional.of(mockStudentEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(updatedLessonDto.getTeacherId(), updatedLessonDto.getDate())).thenReturn(Optional.of(new LessonEntity()));

        // When
        assertThrows(BusyTermLessonException.class, () -> lessonService.updateEntireLesson(id, updatedLessonDto));

        // Then
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
                .setTeacherEntity(new TeacherEntity());

        LessonDto updatedLessonDto = new LessonDto()
                .setId(id)
                .setDate(newDateTime);

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(any(), any())).thenReturn(Optional.empty());
        when(lessonMapper.fromEntityToDto(lessonEntity)).thenReturn(updatedLessonDto);

        // When
        LessonDto result = lessonService.updateLessonDate(id, newDateTime);

        // Then
        assertNotNull(result);
        assertEquals(updatedLessonDto.getId(), result.getId());
        assertEquals(updatedLessonDto.getDate(), result.getDate());
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, times(1)).findByTeacherEntityIdAndDate(any(), any());
        verify(lessonRepository, times(1)).save(lessonEntity);
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
        verify(lessonRepository, never()).findByTeacherEntityIdAndDate(any(), any());
        verify(lessonRepository, never()).save(any());
    }

    @Test
    public void updateLessonDateTest_shouldThrowBusyTermLessonException() {
        // Given
        Long id = faker.number().randomNumber();
        LocalDateTime newDateTime = LocalDateTime.now().plusDays(1);

        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setDate(LocalDateTime.now())
                .setTeacherEntity(new TeacherEntity());

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lessonEntity));
        when(lessonRepository.findByTeacherEntityIdAndDate(any(), any())).thenReturn(Optional.of(new LessonEntity()));

        // When
        assertThrows(BusyTermLessonException.class, () -> lessonService.updateLessonDate(id, newDateTime));

        // Then
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, times(1)).findByTeacherEntityIdAndDate(any(), any());
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
        when(lessonMapper.fromEntityToDto(lessonEntity)).thenReturn(lessonDto);
    }

    private void assertLessonDtoResult(LessonDto expected, LessonDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTeacherId(), actual.getTeacherId());
        assertEquals(expected.getStudentId(), actual.getStudentId());
        assertEquals(expected.getDate(), actual.getDate());
    }

    private LessonDto generateRandomLessonDto() {
        return new LessonDto()
                .setId(faker.number().randomNumber())
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber())
                .setDate(LocalDateTime.now().plusDays(faker.number().numberBetween(1, 365)));
    }
}
