package pl.szlify.codingapi.service;

import java.util.*;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    private Faker faker;

    @BeforeEach
    public void setup() {
        faker = new Faker();
    }

    @Test
    public void getTeachersListTest() {
        // Given
        TeacherEntity teacherEntity = createMockTeacherEntity();
        TeacherBasicInfoDto teacherBasicInfoDto = createMockTeacherBasicInfoDto(teacherEntity);

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
        Long id = faker.number().randomNumber();
        TeacherEntity teacherEntity = createMockTeacherEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        TeacherDto teacherDto = new TeacherDto()
                .setId(teacherEntity.getId())
                .setFirstName(teacherEntity.getFirstName());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.fromEntityToDto(teacherEntity)).thenReturn(teacherDto);

        // When
        TeacherDto result = teacherService.getTeacher(id);

        // Then
        assertEquals(teacherDto, result);
    }

    @Test
    void getTeacher_shouldThrowsLackOfTeacherException() {
        // Given
        Long teacherId = 1L;
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> teacherService.getTeacher(teacherId));
    }

    @Test
    public void addTeacherTest() {
        // Given
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto();
        teacherBasicInfoDto.setId(faker.number().randomNumber());
        teacherBasicInfoDto.setFirstName(faker.name().fullName());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherBasicInfoDto.getId())
                .setFirstName(teacherBasicInfoDto.getFirstName());

        TeacherDto teacherDto = new TeacherDto()
                .setId(teacherEntity.getId())
                .setFirstName(teacherEntity.getFirstName());

        when(teacherMapper.fromNajwInfoToEntity(teacherBasicInfoDto)).thenReturn(teacherEntity);
        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);
        when(teacherMapper.fromEntityToDto(teacherEntity)).thenReturn(teacherDto);

        // When
        TeacherDto result = teacherService.addTeacher(teacherBasicInfoDto);

        // Then
        assertEquals(teacherDto, result);
    }

    @Test
    public void updateEntireTeacherTest() {
        // Given
        Long id = faker.number().randomNumber();
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setId(id)
                .setFirstName(faker.name().fullName());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        TeacherEntity updatedTeacherEntity = new TeacherEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.fromNajwInfoAndEntityToEntity(teacherEntity, teacherBasicInfoDto)).thenReturn(updatedTeacherEntity);
        when(teacherRepository.save(updatedTeacherEntity)).thenReturn(updatedTeacherEntity);
        when(teacherMapper.fromEntityToNajwInfoDto(updatedTeacherEntity)).thenReturn(teacherBasicInfoDto);

        // When
        TeacherBasicInfoDto result = teacherService.updateEntireTeacher(id, teacherBasicInfoDto);

        // Then
        assertEquals(teacherBasicInfoDto, result);
    }

    @Test
    void updateEntireTeacher_ThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        List<String> languagesList = Arrays.asList(faker.lorem().word(), faker.lorem().word());
        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setId(teacherId)
                .setLanguages(languagesList);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> teacherService.updateEntireTeacher(teacherId, teacherBasicInfoDto));
    }

    @Test
    public void updateTeacherLanguagesListTest() {
        // Given
        Long id = faker.number().randomNumber();
        List<String> languagesList = Arrays.asList(faker.lorem().word(), faker.lorem().word());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(id)
                .setLanguages(new ArrayList<>());

        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setId(id)
                .setLanguages(languagesList);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);
        when(teacherMapper.fromEntityToNajwInfoDto(teacherEntity)).thenReturn(teacherBasicInfoDto);

        // When
        TeacherBasicInfoDto result = teacherService.updateTeacherLanguagesList(id, languagesList);

        // Then
        assertEquals(teacherBasicInfoDto, result);
    }

    @Test
    void updateTeacherLanguagesList_ThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        List<String> languagesList = List.of("java", "python");
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> teacherService.updateTeacherLanguagesList(teacherId, languagesList));
    }

    @Test
    public void deleteTeacherTest() {
        // Given
        Long id = faker.number().randomNumber();

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(id)
                .setRemoved(false);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.findByTeacherEntityId(id)).thenReturn(Optional.empty());
        when(teacherRepository.save(teacherEntity)).thenAnswer(i -> i.getArguments()[0]);

        // When
        teacherService.deleteTeacher(id);

        // Then
        assertTrue(teacherEntity.getRemoved().equals(true));
    }

    @Test
    void deleteTeacher_ThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> teacherService.deleteTeacher(teacherId));
    }

    @Test
    void deleteTeacher_ThrowsLessonInFutureException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity();
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.findByTeacherEntityId(anyLong())).thenReturn(Optional.of(new LessonEntity()));

        // When - Then
        assertThrows(LessonInFutureException.class, () -> teacherService.deleteTeacher(teacherId));
    }

    private TeacherEntity createMockTeacherEntity() {
        return new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().fullName());
    }

    private TeacherBasicInfoDto createMockTeacherBasicInfoDto(TeacherEntity teacherEntity) {
        return new TeacherBasicInfoDto()
                .setId(teacherEntity.getId())
                .setFirstName(teacherEntity.getFirstName());
    }
}
