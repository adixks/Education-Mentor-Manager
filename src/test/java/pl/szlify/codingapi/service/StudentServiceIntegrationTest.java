package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import pl.szlify.codingapi.exceptions.BadLanguageException;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.exceptions.MissingStudentException;
import pl.szlify.codingapi.mapper.StudentMapper;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setId(studentEntity.getId())
                .setFirstName(studentEntity.getFirstName());

        when(studentRepository.findAll()).thenReturn(Collections.singletonList(studentEntity));
        when(studentMapper.fromEntityToBasicInfoDto(studentEntity)).thenReturn(studentBasicInfoDto);

        // When
        List<StudentBasicInfoDto> result = studentService.getStudentsList();

        // Then
        assertEquals(1, result.size());
        assertEquals(studentBasicInfoDto, result.get(0));
    }

    @Test
    public void getStudentTest() {
        // Given
        Long studentId = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        StudentDto studentDto = new StudentDto()
                .setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(studentMapper.fromEntityToDto(studentEntity)).thenReturn(studentDto);

        // When
        StudentDto result = studentService.getStudent(studentId);

        // Then
        assertNotNull(result);
        assertEquals(studentDto.getId(), result.getId());
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

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("java", "python"));

        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.fromBasicInfoDtoToEntity(studentBasicInfoDto)).thenReturn(new StudentEntity());
        when(studentMapper.fromEntityToBasicInfoDto(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity entity = invocation.getArgument(0);
            StudentBasicInfoDto dto = new StudentBasicInfoDto();
            dto.setId(entity.getId());
            return dto;
        });
        when(studentRepository.save(any(StudentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        StudentBasicInfoDto result = studentService.addStudent(studentBasicInfoDto);

        // Then
        assertNotNull(result);
        verify(studentRepository, times(1)).save(any(StudentEntity.class));
    }

    @Test
    public void addStudentTest_shouldThrowsLackOfTeacherException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            studentService.addStudent(studentBasicInfoDto);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).save(any(StudentEntity.class));
    }

    @Test
    public void addStudentTest_shouldThrowsBadLanguageException() {
        // Given
        Long teacherId = faker.number().randomNumber();
        String language = "java";

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("pyhon", "C"));

        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));

        // When - Then
        BadLanguageException exception = assertThrows(BadLanguageException.class, () -> {
            studentService.addStudent(studentBasicInfoDto);
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

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("java", "python"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.fromBasicInfoAndEntityToEntity(studentEntity, studentBasicInfoDto)).thenReturn(new StudentEntity());
        when(studentMapper.fromEntityToDto(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity entity = invocation.getArgument(0);
            StudentDto dto = new StudentDto();
            dto.setId(entity.getId());
            return dto;
        });
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        StudentDto result = studentService.updateEntireStudent(studentId, studentBasicInfoDto);

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

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        // When - Then
        MissingStudentException exception = assertThrows(MissingStudentException.class, () -> {
            studentService.updateEntireStudent(studentId, studentBasicInfoDto);
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

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.empty());

        // When - Then
        LackOfTeacherException exception = assertThrows(LackOfTeacherException.class, () -> {
            studentService.updateEntireStudent(studentId, studentBasicInfoDto);
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

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage(language);

        StudentEntity studentEntity = new StudentEntity()
                .setId(studentId);

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Arrays.asList("python", "C"));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));

        // When - Then
        BadLanguageException exception = assertThrows(BadLanguageException.class, () -> {
            studentService.updateEntireStudent(studentId, studentBasicInfoDto);
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
        when(teacherRepository.findByIdAndRemovedFalse(newTeacherId)).thenReturn(Optional.of(newTeacherEntity));
        when(studentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(studentMapper.fromEntityToBasicInfoDto(any(StudentEntity.class))).thenAnswer(invocation -> {
            StudentEntity entity = invocation.getArgument(0);
            StudentBasicInfoDto dto = new StudentBasicInfoDto();
            dto.setId(entity.getId());
            return dto;
        });

        // When
        StudentBasicInfoDto result = studentService.updateStudentTeacher(studentId, newTeacherId);

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
        when(teacherRepository.findByIdAndRemovedFalse(newTeacherId)).thenReturn(Optional.empty());

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
        when(teacherRepository.findByIdAndRemovedFalse(newTeacherId)).thenReturn(Optional.of(newTeacherEntity));

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

        when(lessonRepository.findByStudentEntityId(studentId)).thenReturn(Optional.empty());

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
        lessonEntity.setStudentEntity(studentEntity);

        when(lessonRepository.findByStudentEntityId(studentId)).thenReturn(Optional.of(lessonEntity));

        // When - Then
        LessonInFutureException exception = assertThrows(LessonInFutureException.class, () -> {
            studentService.deleteStudent(studentId);
        });

        assertNotNull(exception);
        verify(studentRepository, never()).deleteById(anyLong());
    }
}
