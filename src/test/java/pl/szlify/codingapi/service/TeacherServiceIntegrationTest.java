package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
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

        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setId(teacherEntity.getId())
                .setFirstName(teacherEntity.getFirstName());

        when(teacherRepository.findAll()).thenReturn(Collections.singletonList(teacherEntity));
        when(teacherMapper.fromEntityToNajwInfoDto(teacherEntity)).thenReturn(teacherBasicInfoDto);

        // When
        List<TeacherBasicInfoDto> result = teacherService.getTeachersList();

        // Then
        assertEquals(1, result.size());
        assertEquals(teacherBasicInfoDto, result.get(0));
    }

    @Test
    public void getTeacherTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId);

        TeacherDto teacherDto = new TeacherDto()
                .setId(teacherId);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.fromEntityToDto(teacherEntity)).thenReturn(teacherDto);

        // When
        TeacherDto result = teacherService.getTeacher(teacherId);

        // Then
        assertNotNull(result);
        assertEquals(teacherDto.getId(), result.getId());
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
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setFirstName(faker.name().firstName());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(teacherBasicInfoDto.getFirstName());

        when(teacherMapper.fromNajwInfoToEntity(teacherBasicInfoDto)).thenReturn(teacherEntity);
        when(teacherMapper.fromEntityToDto(teacherEntity)).thenReturn(new TeacherDto());
        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);

        // When
        TeacherDto result = teacherService.addTeacher(teacherBasicInfoDto);

        // Then
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(any(TeacherEntity.class));
    }

    @Test
    public void updateEntireTeacherTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setFirstName(faker.name().firstName());

        TeacherEntity existingTeacherEntity = new TeacherEntity()
                .setId(teacherId);

        TeacherEntity updatedTeacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setFirstName(teacherBasicInfoDto.getFirstName());

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(existingTeacherEntity));
        when(teacherMapper.fromNajwInfoAndEntityToEntity(existingTeacherEntity, teacherBasicInfoDto))
                .thenReturn(updatedTeacherEntity);
        when(teacherMapper.fromEntityToNajwInfoDto(updatedTeacherEntity)).thenReturn(new TeacherBasicInfoDto());
        when(teacherRepository.save(updatedTeacherEntity)).thenReturn(updatedTeacherEntity);

        // When
        TeacherBasicInfoDto result = teacherService.updateEntireTeacher(teacherId, teacherBasicInfoDto);

        // Then
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(any(TeacherEntity.class));
    }

    @Test
    public void updateEntireTeacherTest_shouldThrowsException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setFirstName(faker.name().firstName());

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            teacherService.updateEntireTeacher(teacherId, teacherBasicInfoDto);
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
        when(teacherMapper.fromEntityToNajwInfoDto(any())).thenAnswer(invocation -> {
            TeacherEntity entity = invocation.getArgument(0);
            TeacherBasicInfoDto dto = new TeacherBasicInfoDto()
                    .setId(entity.getId())
                    .setFirstName(entity.getFirstName());
            return dto;
        });
        when(teacherRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TeacherBasicInfoDto result = teacherService.updateTeacherLanguagesList(teacherId, languagesList);

        // Then
        assertNotNull(result);
        verify(teacherRepository, times(1)).save(any(TeacherEntity.class));
        assertEquals(expectedLanguagesList, teacherEntity.getLanguages());
    }

    @Test
    public void deleteTeacherTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setRemoved(false);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.findByTeacherEntityId(teacherId)).thenReturn(Optional.empty());

        // When
        teacherService.deleteTeacher(teacherId);

        // Then
        assertTrue(teacherEntity.getRemoved());
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
        verify(lessonRepository, never()).findByTeacherEntityId(anyLong());
        verify(teacherRepository, never()).save(any(TeacherEntity.class));
    }

    @Test
    public void deleteTeacherTest_shouldThrowsLessonInFutureException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setRemoved(false);

        LessonEntity lessonEntity = new LessonEntity()
                .setTeacherEntity(teacherEntity);

        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.findByTeacherEntityId(teacherId)).thenReturn(Optional.of(lessonEntity));

        // When and Then
        LessonInFutureException exception = assertThrows(LessonInFutureException.class, () -> {
            teacherService.deleteTeacher(teacherId);
        });

        assertNotNull(exception);
        verify(teacherRepository, never()).save(any(TeacherEntity.class));
    }
}
