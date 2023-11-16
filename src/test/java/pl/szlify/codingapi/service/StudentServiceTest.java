package pl.szlify.codingapi.service;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.exceptions.BadLanguageException;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.exceptions.MissingStudentException;
import pl.szlify.codingapi.mapper.StudentMapper;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.Collections;
import java.util.List;
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
        Long id = faker.number().randomNumber();
        StudentEntity studentEntity = new StudentEntity()
                .setId(id)
                .setFirstName(faker.name().fullName());

        StudentDto studentDto = new StudentDto()
                .setId(studentEntity.getId())
                .setFirstName(studentEntity.getFirstName());

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentEntity));
        when(studentMapper.fromEntityToDto(studentEntity)).thenReturn(studentDto);

        // When
        StudentDto result = studentService.getStudent(id);

        // Then
        assertEquals(studentDto, result);
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
        verify(studentMapper, never()).fromEntityToDto(any());
    }

    @Test
    public void addStudentTest() {
        // Given
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Collections.singletonList("java"));

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage("java");

        StudentEntity studentEntity = new StudentEntity()
                .setTeacherEntity(teacherEntity);

        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.fromBasicInfoDtoToEntity(studentBasicInfoDto)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.fromEntityToBasicInfoDto(studentEntity)).thenReturn(studentBasicInfoDto);

        // When
        StudentBasicInfoDto result = studentService.addStudent(studentBasicInfoDto);

        // Then
        assertEquals(studentBasicInfoDto, result);
    }

    @Test
    void addStudentTest_shouldThrowsLackOfTeacherException() {
        // Given
        StudentBasicInfoDto studentBasicInfoDto = createFakeStudentBasicInfoDto();
        when(teacherRepository.findByIdAndRemovedFalse(any())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> studentService.addStudent(studentBasicInfoDto));
    }

    @Test
    void addStudent_ThrowsBadLanguageException() {
        // Given
        StudentBasicInfoDto studentBasicInfoDto = createFakeStudentBasicInfoDto();
        when(teacherRepository.findByIdAndRemovedFalse(any())).thenReturn(Optional.of(teacherEntity));
        when(teacherEntity.getLanguages()).thenReturn(Collections.emptyList());

        // When - Then
        assertThrows(BadLanguageException.class, () -> studentService.addStudent(studentBasicInfoDto));
    }

    @Test
    public void updateEntireStudentTest() {
        // Given
        Long id = faker.number().randomNumber();
        Long teacherId = faker.number().randomNumber();
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(teacherId)
                .setLanguages(Collections.singletonList("java"));

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setTeacherId(teacherId)
                .setLanguage("java");

        StudentEntity studentEntity = new StudentEntity()
                .setId(id)
                .setTeacherEntity(teacherEntity);

        StudentDto studentDto = new StudentDto()
                .setId(id)
                .setTeacherId(teacherId)
                .setLanguage("java");

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentMapper.fromBasicInfoAndEntityToEntity(studentEntity, studentBasicInfoDto)).thenReturn(studentEntity);
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.fromEntityToDto(studentEntity)).thenReturn(studentDto);

        // When
        StudentDto result = studentService.updateEntireStudent(id, studentBasicInfoDto);

        // Then
        assertEquals(studentDto, result);
    }

    @Test
    void updateEntireStudent_ThrowsMissingStudentException() {
        // Given
        Long studentId = 1L;
        StudentBasicInfoDto studentBasicInfoDto = createFakeStudentBasicInfoDto();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(MissingStudentException.class, () -> studentService.updateEntireStudent(studentId, studentBasicInfoDto));
    }

    @Test
    void updateEntireStudent_ThrowsLackOfTeacherException() {
        // Given
        Long studentId = 1L;
        StudentBasicInfoDto studentBasicInfoDto = createFakeStudentBasicInfoDto();
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(new StudentEntity()));
        when(teacherRepository.findByIdAndRemovedFalse(any())).thenReturn(Optional.empty());

        // When - Then
        assertThrows(LackOfTeacherException.class, () -> studentService.updateEntireStudent(studentId, studentBasicInfoDto));
    }

    @Test
    void updateEntireStudent_ThrowsBadLanguageException() {
        // Given
        Long studentId = 1L;
        StudentBasicInfoDto studentBasicInfoDto = createFakeStudentBasicInfoDto();
        StudentEntity studentEntity = new StudentEntity();

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndRemovedFalse(any())).thenReturn(Optional.of(teacherEntity));
        when(teacherEntity.getLanguages()).thenReturn(Collections.emptyList());

        // When - Then
        assertThrows(BadLanguageException.class, () -> studentService.updateEntireStudent(studentId, studentBasicInfoDto));
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
                .setTeacherEntity(teacherEntity);

        StudentBasicInfoDto studentBasicInfoDto = new StudentBasicInfoDto()
                .setId(id)
                .setTeacherId(teacherId)
                .setLanguage("java");

        when(studentRepository.findById(id)).thenReturn(Optional.of(studentEntity));
        when(teacherRepository.findByIdAndRemovedFalse(teacherId)).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.save(studentEntity)).thenReturn(studentEntity);
        when(studentMapper.fromEntityToBasicInfoDto(studentEntity)).thenReturn(studentBasicInfoDto);

        // When
        StudentBasicInfoDto result = studentService.updateStudentTeacher(id, teacherId);

        // Then
        assertEquals(studentBasicInfoDto, result);
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
        when(teacherRepository.findByIdAndRemovedFalse(anyLong())).thenReturn(Optional.empty());

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
        when(teacherRepository.findByIdAndRemovedFalse(anyLong())).thenReturn(Optional.of(teacherEntity));

        // When - Then
        assertThrows(BadLanguageException.class, () -> studentService.updateStudentTeacher(studentId, teacherId));
    }

    @Test
    public void deleteStudentTest() {
        // Given
        Long id = faker.number().randomNumber();
        when(lessonRepository.findByStudentEntityId(id)).thenReturn(Optional.empty());

        // When
        studentService.deleteStudent(id);

        // Then
        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteStudent_ThrowsLessonInFutureException() {
        // Given
        Long studentId = 1L;
        when(lessonRepository.findByStudentEntityId(anyLong())).thenReturn(Optional.of(new LessonEntity()));

        // When - Then
        assertThrows(LessonInFutureException.class, () -> studentService.deleteStudent(studentId));
        verify(studentRepository, never()).deleteById(anyLong());
    }

    private StudentBasicInfoDto createFakeStudentBasicInfoDto() {
        return new StudentBasicInfoDto()
                .setId(faker.random().nextLong())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word())
                .setTeacherId(faker.random().nextLong());
    }
}
