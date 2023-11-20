package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeacherServiceIntegrationTest {

    @Autowired
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private TeacherMapper teacherMapper;

    private static final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTeachersListTest() {
        // Given
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().fullName());

        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName(teacherEntity.getFirstName());

        Pageable pageable = PageRequest.of(0, 5);
        Page<TeacherEntity> pageOfEntities = new PageImpl<>(Collections.singletonList(teacherEntity));
        when(teacherRepository.findAll(pageable)).thenReturn(pageOfEntities);
        when(teacherMapper.toShortDto(teacherEntity)).thenReturn(teacherShortDto);

        // When
        Page<TeacherShortDto> result = teacherService.getList(pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(teacherShortDto, result.getContent().get(0));
    }

    @Test
    public void getTeacherTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId);

        TeacherFullDto teacherFullDto = new TeacherFullDto()
                .setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.toFullDto(teacherEntity)).thenReturn(teacherFullDto);

        // When
        TeacherFullDto result = teacherService.getTeacher(teacherId);

        // Then
        assertNotNull(result);
        assertEquals(teacherFullDto.getId(), result.getId());
    }

    @Test
    public void getTeacherTest_shouldThrowsException() {
        // Given
        Long teacherId = faker.number().randomNumber();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            teacherService.getTeacher(teacherId);
        });

        assertNotNull(exception);
        assertEquals("The teacher with the specified ID does not exist", exception.getMessage());
    }

    @Test
    public void addTeacherTest() {
        // Given
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName(faker.name().firstName());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(teacherShortDto.getFirstName());

        when(teacherMapper.toEntity(teacherShortDto)).thenReturn(teacherEntity);
        when(teacherMapper.toFullDto(teacherEntity)).thenReturn(new TeacherFullDto());
        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);

        // When
        TeacherFullDto result = teacherService.addTeacher(teacherShortDto);

        // Then
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(any(TeacherEntity.class));
    }

    @Test
    public void updateEntireTeacherTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName(faker.name().firstName());

        TeacherEntity existingTeacherEntity = new TeacherEntity()
                .setId(teacherId);

        TeacherEntity updatedTeacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setFirstName(teacherShortDto.getFirstName());

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(existingTeacherEntity));
        when(teacherMapper.toEntityUpdate(existingTeacherEntity, teacherShortDto))
                .thenReturn(updatedTeacherEntity);
        when(teacherMapper.toShortDto(updatedTeacherEntity)).thenReturn(new TeacherShortDto());
        when(teacherRepository.save(updatedTeacherEntity)).thenReturn(updatedTeacherEntity);

        // When
        TeacherShortDto result = teacherService.updateEntireTeacher(teacherId, teacherShortDto);

        // Then
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(any(TeacherEntity.class));
    }

    @Test
    public void updateEntireTeacherTest_shouldThrowsException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName(faker.name().firstName());

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            teacherService.updateEntireTeacher(teacherId, teacherShortDto);
        });

        assertNotNull(exception);
        assertEquals("The teacher with the specified ID does not exist", exception.getMessage());
    }

    @Test
    public void updateTeacherLanguagesListTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        List<String> languagesList = Arrays.asList("java", "python");
        List<String> expectedLanguagesList = Arrays.asList("C++", "C", "java", "python");


        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("C++", "C"));

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.toShortDto(any())).thenAnswer(invocation -> {
            TeacherEntity entity = invocation.getArgument(0);
            TeacherShortDto dto = new TeacherShortDto()
                    .setFirstName(entity.getFirstName());
            return dto;
        });
        when(teacherRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TeacherShortDto result = teacherService.updateTeacherLanguagesList(teacherId, languagesList);

        // Then
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(any(TeacherEntity.class));
        assertEquals(expectedLanguagesList, teacherEntity.getLanguages());
    }

    @Test
    public void updateTeacherLanguagesListTest_shouldThrowsException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        List<String> languagesList = Arrays.asList("java", "python");

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            teacherService.updateTeacherLanguagesList(teacherId, languagesList);
        });

        assertNotNull(exception);
        assertEquals("The teacher with the specified ID does not exist", exception.getMessage());
    }

    @Test
    public void deleteTeacherTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setDeleted(false);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.existsByTeacherId(teacherId)).thenReturn(false);

        // When
        teacherService.deleteTeacher(teacherId);

        // Then
        assertTrue(teacherEntity.getDeleted());
        verify(teacherRepository, times(1)).save(teacherEntity);
    }

    @Test
    public void deleteTeacherTest_shouldThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            teacherService.deleteTeacher(teacherId);
        });

        assertNotNull(exception);
        assertEquals("The teacher with the specified ID does not exist", exception.getMessage());
        verify(lessonRepository, never()).existsByTeacherId(anyLong());
        verify(teacherRepository, never()).save(any(TeacherEntity.class));
    }

    @Test
    public void deleteTeacherTest_shouldThrowsLessonInFutureException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setDeleted(false);

        LessonEntity lessonEntity = new LessonEntity()
                .setTeacher(teacherEntity);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.existsByTeacherId(teacherId)).thenReturn(true);

        // When - Then
        LessonInFutureException exception = assertThrows(LessonInFutureException.class, () -> {
            teacherService.deleteTeacher(teacherId);
        });

        assertNotNull(exception);
        verify(teacherRepository, never()).save(any(TeacherEntity.class));
    }
}
