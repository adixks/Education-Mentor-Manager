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
import org.springframework.test.context.ActiveProfiles;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class StudentServiceIntegrationTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private StudentMapper studentMapper;

    private static final Faker faker = new Faker();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getStudentsListTest() {
        // Given
        StudentEntity studentEntity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().fullName());

        StudentShortDto studentShortDto = new StudentShortDto()
                .setFirstName(studentEntity.getFirstName());

        Page<StudentEntity> pagedResponse = new PageImpl<>(Collections.singletonList(studentEntity));
        Pageable pageable = PageRequest.of(0, 5);
        when(studentRepository.findAll(pageable)).thenReturn(pagedResponse);
        when(studentMapper.toShortDto(studentEntity)).thenReturn(studentShortDto);

        // When
        Page<StudentShortDto> result = studentService.getList(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(studentShortDto, result.getContent().get(0));
    }

    @Test
    public void getStudentTest() {
        // Given
        Long studentId = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        StudentFullDto studentFullDto = new StudentFullDto()
                .setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(studentMapper.toFullDto(studentEntity)).thenReturn(studentFullDto);

        // When
        StudentFullDto result = studentService.getStudent(studentId);

        // Then
        assertNotNull(result);
        assertEquals(studentFullDto.getId(), result.getId());
    }

    @Test
    public void getStudentTest_shouldThrowsException() {
        // Given
        Long studentId = faker.number().randomNumber();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When - Then
        MissingStudentException exception = assertThrows(MissingStudentException.class, () -> {
            studentService.getStudent(studentId);
        });

        assertNotNull(exception);
        assertEquals("The student with the given ID does not exist", exception.getMessage());
    }

    @Test
    public void addStudentTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("java", "python"));

        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.toEntity(studentShortDto)).thenReturn(new StudentEntity());
        when(studentMapper.toShortDto(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity entity = invocation.getArgument(0);
            StudentShortDto dto = new StudentShortDto();
            return dto;
        });
        when(studentRepository.save(any(StudentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        StudentShortDto result = studentService.addStudent(studentShortDto);

        // Then
        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(StudentEntity.class));
    }

    @Test
    public void addStudentTest_shouldThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            studentService.addStudent(studentShortDto);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void addStudentTest_shouldThrowsBadLanguageException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("pyhon", "C"));

        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));

        // When - Then
        BadLanguageException exception = assertThrows(BadLanguageException.class, () -> {
            studentService.addStudent(studentShortDto);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void updateEntireStudentTest() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("java", "python"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.toEntityUpdate(studentEntity, studentShortDto)).thenReturn(new StudentEntity());
        when(studentMapper.toFullDto(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity entity = invocation.getArgument(0);
            StudentFullDto dto = new StudentFullDto();
            dto.setId(entity.getId());
            return dto;
        });
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        StudentFullDto result = studentService.updateEntireStudent(studentId, studentShortDto);

        // Then
        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(StudentEntity.class));
    }

    @Test
    public void updateEntireStudentTest_shouldThrowsMissingStudentException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When - Then
        MissingStudentException exception = assertThrows(MissingStudentException.class, () -> {
            studentService.updateEntireStudent(studentId, studentShortDto);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void updateEntireStudentTest_shouldThrowsLackOfTeacherException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            studentService.updateEntireStudent(studentId, studentShortDto);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void updateEntireStudentTest_shouldThrowsBadLanguageException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentShortDto studentShortDto = new StudentShortDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("python", "C"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));

        // When - Then
        BadLanguageException exception = assertThrows(BadLanguageException.class, () -> {
            studentService.updateEntireStudent(studentId, studentShortDto);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void updateStudentTeacherTest() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long newTeacherId = faker.number().randomNumber();

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId)
                .setLanguage("java");

        TeacherEntity newTeacherEntity = new TeacherEntity()
                .setId(newTeacherId)
                .setLanguages(Arrays.asList("java", "python"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(newTeacherId)).thenReturn(Optional.of(newTeacherEntity));
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentMapper.toShortDto(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity entity = invocation.getArgument(0);
            StudentShortDto dto = new StudentShortDto();
            return dto;
        });

        // When
        StudentShortDto result = studentService.updateStudentTeacher(studentId, newTeacherId);

        // Then
        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(StudentEntity.class));
    }

    @Test
    public void updateStudentTeacherTest_shouldThrowsMissingStudentException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long newTeacherId = faker.number().randomNumber();

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When - Then
        MissingStudentException exception = assertThrows(MissingStudentException.class, () -> {
            studentService.updateStudentTeacher(studentId, newTeacherId);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void updateStudentTeacherTest_shouldThrowsLackOfTeacherException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long newTeacherId = faker.number().randomNumber();

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(newTeacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            studentService.updateStudentTeacher(studentId, newTeacherId);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void updateStudentTeacherTest_shouldThrowsBadLanguageException() {
        // Given
        Long studentId = faker.number().randomNumber();
        Long newTeacherId = faker.number().randomNumber();

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId)
                .setLanguage("java");

        TeacherEntity newTeacherEntity = new TeacherEntity()
                .setId(newTeacherId)
                .setLanguages(Arrays.asList("python", "C"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndDeletedFalse(newTeacherId)).thenReturn(Optional.of(newTeacherEntity));

        // When and Then
        BadLanguageException exception = assertThrows(BadLanguageException.class, () -> {
            studentService.updateStudentTeacher(studentId, newTeacherId);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void deleteStudentTest() {
        // Given
        Long studentId = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentId);

        when(lessonRepository.existsByStudentId(studentId)).thenReturn(false);

        // When
        studentService.deleteStudent(studentId);

        // Then
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    public void deleteStudentThrowsLessonInFutureExceptionTest() {
        // Given
        Long studentId = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId(studentId);

        LessonEntity lessonEntity = new LessonEntity();
        lessonEntity.setStudent(studentEntity);

        when(lessonRepository.existsByStudentId(studentId)).thenReturn(true);

        // When - Then
        LessonInFutureException exception = assertThrows(LessonInFutureException.class, () -> {
            studentService.deleteStudent(studentId);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).deleteById(anyLong());
    }
}
