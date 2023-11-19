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
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
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
        TeacherShortDto teacherShortDto = createMockTeacherBasicInfoDto(teacherEntity);

        when(teacherRepository.findAll()).thenReturn(Collections.singletonList(teacherEntity));
        when(teacherMapper.toShortDto(teacherEntity)).thenReturn(teacherShortDto);

        // When
        List<TeacherShortDto> result = teacherService.getTeachersList();

        // Then
        assertEquals(1, result.size());
        assertEquals(teacherShortDto, result.get(0));
    }

    @Test
    public void getTeacherTest() {
        // Given
        Long id = faker.number().randomNumber();
        TeacherEntity teacherEntity = createMockTeacherEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        TeacherFullDto teacherFullDto = new TeacherFullDto()
                .setId(teacherEntity.getId())
                .setFirstName(teacherEntity.getFirstName());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.toFullDto(teacherEntity)).thenReturn(teacherFullDto);

        // When
        TeacherFullDto result = teacherService.getTeacher(id);

        // Then
        assertEquals(teacherFullDto, result);
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
        TeacherShortDto teacherShortDto = new TeacherShortDto();
        teacherShortDto.setFirstName(faker.name().fullName());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setFirstName(teacherShortDto.getFirstName());

        TeacherFullDto teacherFullDto = new TeacherFullDto()
                .setId(teacherEntity.getId())
                .setFirstName(teacherEntity.getFirstName());

        when(teacherMapper.toEntity(teacherShortDto)).thenReturn(teacherEntity);
        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);
        when(teacherMapper.toFullDto(teacherEntity)).thenReturn(teacherFullDto);

        // When
        TeacherFullDto result = teacherService.addTeacher(teacherShortDto);

        // Then
        assertEquals(teacherFullDto, result);
    }

    @Test
    public void updateEntireTeacherTest() {
        // Given
        Long id = faker.number().randomNumber();
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName(faker.name().fullName());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        TeacherEntity updatedTeacherEntity = new TeacherEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherMapper.toEntityUpdate(teacherEntity, teacherShortDto)).thenReturn(updatedTeacherEntity);
        when(teacherRepository.save(updatedTeacherEntity)).thenReturn(updatedTeacherEntity);
        when(teacherMapper.toShortDto(updatedTeacherEntity)).thenReturn(teacherShortDto);

        // When
        TeacherShortDto result = teacherService.updateEntireTeacher(id, teacherShortDto);

        // Then
        assertEquals(teacherShortDto, result);
    }

    @Test
    void updateEntireTeacher_ThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        List<String> languagesList = Arrays.asList(faker.lorem().word(), faker.lorem().word());
        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setLanguages(languagesList);
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> teacherService.updateEntireTeacher(teacherId, teacherShortDto));
    }

    @Test
    public void updateTeacherLanguagesListTest() {
        // Given
        Long id = faker.number().randomNumber();
        List<String> languagesList = Arrays.asList(faker.lorem().word(), faker.lorem().word());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(id)
                .setLanguages(new ArrayList<>());

        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setLanguages(languagesList);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(teacherRepository.save(teacherEntity)).thenReturn(teacherEntity);
        when(teacherMapper.toShortDto(teacherEntity)).thenReturn(teacherShortDto);

        // When
        TeacherShortDto result = teacherService.updateTeacherLanguagesList(id, languagesList);

        // Then
        assertEquals(teacherShortDto, result);
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
                .setDeleted(false);

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacherEntity));
        when(lessonRepository.existsByTeacherId(id)).thenReturn(false);
        when(teacherRepository.save(teacherEntity)).thenAnswer(i -> i.getArguments()[0]);

        // When
        teacherService.deleteTeacher(id);

        // Then
        assertTrue(teacherEntity.getDeleted().equals(true));
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
        when(lessonRepository.existsByTeacherId(anyLong())).thenReturn(true);

        // When - Then
        assertThrows(LessonInFutureException.class, () -> teacherService.deleteTeacher(teacherId));
    }

    private TeacherEntity createMockTeacherEntity() {
        return new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().fullName());
    }

    private TeacherShortDto createMockTeacherBasicInfoDto(TeacherEntity teacherEntity) {
        return new TeacherShortDto()
                .setFirstName(teacherEntity.getFirstName());
    }
}
