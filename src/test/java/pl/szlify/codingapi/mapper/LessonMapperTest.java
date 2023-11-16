package pl.szlify.codingapi.mapper;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class LessonMapperTest {

    @InjectMocks
    private LessonMapper lessonMapper;

    @Mock
    private TeacherEntity teacherEntity;

    @Mock
    private StudentEntity studentEntity;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        faker = new Faker();
    }

    @Test
    public void testFromEntityToDto() {
        // Given
        LessonEntity entity = new LessonEntity()
                .setId(faker.number().randomNumber())
                .setDate(LocalDateTime.now())
                .setTeacherEntity(teacherEntity)
                .setStudentEntity(studentEntity);

        when(teacherEntity.getId()).thenReturn(faker.number().randomNumber());
        when(studentEntity.getId()).thenReturn(faker.number().randomNumber());

        // When
        LessonDto dto = lessonMapper.fromEntityToDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getDate(), dto.getDate());
        assertEquals(entity.getTeacherEntity().getId(), dto.getTeacherId());
        assertEquals(entity.getStudentEntity().getId(), dto.getStudentId());
    }

    @Test
    public void testFromDtoToEntity() {
        // Given
        Faker faker = new Faker();
        LessonDto lessonDto = new LessonDto()
                .setId(faker.number().randomNumber())
                .setDate(LocalDateTime.now())
                .setTeacherId(faker.number().randomNumber())
                .setStudentId(faker.number().randomNumber());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(lessonDto.getTeacherId())
                .setFirstName(faker.name().fullName());

        StudentEntity studentEntity = new StudentEntity()
                .setId(lessonDto.getStudentId())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName());

        when(teacherRepository.findById(lessonDto.getTeacherId())).thenReturn(Optional.of(teacherEntity));
        when(studentRepository.findById(lessonDto.getStudentId())).thenReturn(Optional.of(studentEntity));

        // When
        LessonEntity lessonEntity = lessonMapper.fromDtoToEntity(lessonDto);

        // Then
        assertEquals(lessonDto.getId(), lessonEntity.getId());
        assertEquals(lessonDto.getDate(), lessonEntity.getDate());
    }
}
