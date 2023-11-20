package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.szlify.codingapi.exceptions.BadLanguageException;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.exceptions.MissingStudentException;
import pl.szlify.codingapi.mapper.StudentMapper;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.model.dto.StudentFullDto;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private TeacherEntity teacherEntity;

    @InjectMocks
    private StudentService studentService;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    public void getStudentsListTest() {
        // Given
        StudentEntity studentEntity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().fullName());

        StudentShortDto studentShortDto = new StudentShortDto()
                .setFirstName(studentEntity.getFirstName());

        Pageable pageable = PageRequest.of(0, 5);
        Page<StudentEntity> pageOfEntities = new PageImpl<>(Collections.singletonList(studentEntity));
        when(studentRepository.findAll(pageable)).thenReturn(pageOfEntities);
        when(studentMapper.toShortDto(studentEntity)).thenReturn(studentShortDto);

        // When
        Page<StudentShortDto> result = studentService.getStudentsList(pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(studentShortDto, result.getContent().get(0));
    }

    @Test
    public void getStudentTest() {
        // Given
        Long id = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        StudentFullDto studentFullDto = new StudentFullDto()
                .setId(studentEntity.getId())
                .setFirstName(studentEntity.getFirstName());

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentEntity));
        when(studentMapper.toFullDto(studentEntity)).thenReturn(studentFullDto);

        // When
        StudentFullDto result = studentService.getStudent(id);

        // Then
        assertEquals(studentFullDto, result);
    }

    @Test
    public void getStudentTest_shouldThrowMissingStudentException() {
        // Given
        Long nonExistingStudentId = faker.number().randomNumber();
        when(studentRepository.findById(nonExistingStudentId)).thenReturn(Optional.empty());

        // When
        assertThrows(MissingStudentException.class, () -> studentService.getStudent(nonExistingStudentId));

        // Then
        verify(studentRepository, times(1)).findById(nonExistingStudentId);
        verify(studentMapper, never()).toFullDto(any());
    }

    @Test
    public void addStudentTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Collections.singletonList("java"));

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage("java");

        StudentEntity studentEntity = new StudentEntity()
                .setTeacher(teacherEntity);

        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.toEntity(studentShortDto)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.toShortDto(studentEntity)).thenReturn(studentShortDto);

        // When
        StudentShortDto result = studentService.addStudent(studentShortDto);

        // Then
        assertEquals(studentShortDto, result);
    }

    @Test
    void addStudentTest_shouldThrowsLackOfTeacherException() {
        // Given
        StudentShortDto studentShortDto = createFakeStudentBasicInfoDto();
        when(teacherRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> studentService.addStudent(studentShortDto));
    }

    @Test
    void addStudent_ThrowsBadLanguageException() {
        // Given
        StudentShortDto studentShortDto = createFakeStudentBasicInfoDto();
        when(teacherRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(teacherEntity));
        when(teacherEntity.getLanguages()).thenReturn(Collections.emptyList());

        // When - Then
        assertThrows(BadLanguageException.class, () -> studentService.addStudent(studentShortDto));
    }

    @Test
    public void updateEntireStudentTest() {
        // Given
        Long id = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Collections.singletonList("java"));

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage("java");

        StudentEntity studentEntity = new StudentEntity()
                .setId(id)
                .setTeacher(teacherEntity);

        StudentFullDto studentFullDto = new StudentFullDto()
                .setId(id)
                .setTeacherId(teacherId)
                .setLanguage("java");

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.toEntityUpdate(studentEntity, studentShortDto)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.toFullDto(studentEntity)).thenReturn(studentFullDto);

        // When
        StudentFullDto result = studentService.updateEntireStudent(id, studentShortDto);

        // Then
        assertEquals(studentFullDto, result);
    }

    @Test
    void updateEntireStudent_ThrowsMissingStudentException() {
        // Given
        Long studentId = 1L;
        StudentShortDto studentShortDto = createFakeStudentBasicInfoDto();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(MissingStudentException.class, () -> studentService.updateEntireStudent(studentId, studentShortDto));
    }

    @Test
    void updateEntireStudent_ThrowsLackOfTeacherException() {
        // Given
        Long studentId = 1L;
        StudentShortDto studentShortDto = createFakeStudentBasicInfoDto();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(new StudentEntity()));
        when(teacherRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> studentService.updateEntireStudent(studentId, studentShortDto));
    }

    @Test
    void updateEntireStudent_ThrowsBadLanguageException() {
        // Given
        Long studentId = 1L;
        StudentShortDto studentShortDto = createFakeStudentBasicInfoDto();
        StudentEntity studentEntity = new StudentEntity();

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(teacherEntity));
        when(teacherEntity.getLanguages()).thenReturn(Collections.emptyList());

        // When - Then
        assertThrows(BadLanguageException.class, () -> studentService.updateEntireStudent(studentId, studentShortDto));
    }

    @Test
    public void updateStudentTeacherTest() {
        // Given
        Long id = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Collections.singletonList("java"));

        StudentEntity studentEntity = new StudentEntity()
                .setId(id)
                .setLanguage("java")
                .setTeacher(teacherEntity);

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage("java");

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.toShortDto(studentEntity)).thenReturn(studentShortDto);

        // When
        StudentShortDto result = studentService.updateStudentTeacher(id, teacherId);

        // Then
        assertEquals(studentShortDto, result);
    }

    @Test
    void updateStudentTeacher_ThrowsMissingStudentException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(MissingStudentException.class, () -> studentService.updateStudentTeacher(studentId, teacherId));
    }

    @Test
    void updateStudentTeacher_ThrowsLackOfTeacherException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> studentService.updateStudentTeacher(studentId, teacherId));
    }

    @Test
    void updateStudentTeacher_ThrowsBadLanguageException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity()
                .setLanguage("java");

        TeacherEntity teacherEntity = new TeacherEntity()
                .setLanguages(Collections.singletonList("python"));

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(anyLong())).thenReturn(Optional.of(teacherEntity));

        // When - Then
        assertThrows(BadLanguageException.class, () -> studentService.updateStudentTeacher(studentId, teacherId));
    }

    @Test
    public void deleteStudentTest() {
        // Given
        Long id = faker.number().randomNumber();
        when(lessonRepository.existsByStudentId(id)).thenReturn(false);

        // When
        studentService.deleteStudent(id);

        // Then
        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteStudent_ThrowsLessonInFutureException() {
        // Given
        Long studentId = 1L;
        when(lessonRepository.existsByStudentId(anyLong())).thenReturn(true);

        // When - Then
        assertThrows(LessonInFutureException.class, () -> studentService.deleteStudent(studentId));
        verify(studentRepository, never()).deleteById(anyLong());
    }

    private StudentShortDto createFakeStudentBasicInfoDto() {
        return new StudentShortDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word())
                .setTeacherId(faker.random().nextLong());
    }
}
