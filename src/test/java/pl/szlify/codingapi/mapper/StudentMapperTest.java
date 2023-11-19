package pl.szlify.codingapi.mapper;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.model.dto.StudentFullDto;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentMapperTest {

    private Faker faker;

    @InjectMocks
    private StudentMapper studentMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        faker = new Faker();
    }

    @Test
    public void testFromEntityToDto() {
        // Given
        StudentEntity entity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word())
                .setDeleted(faker.bool().bool());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber());

        entity.setTeacher(teacherEntity);

        Set<LessonEntity> lessons = new HashSet<>();
        LessonEntity lesson = new LessonEntity().setId(faker.number().randomNumber());
        lessons.add(lesson);
        entity.setLessons(lessons);

        // When
        StudentFullDto dto = studentMapper.toFullDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getLanguage(), dto.getLanguage());
        assertEquals(entity.getDeleted(), dto.getRemoved());
    }

    @Test
    public void testFromEntityToBasicInfoDto() {
        // Given
        StudentEntity entity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber());

        entity.setTeacher(teacherEntity);

        // When
        StudentShortDto dto = studentMapper.toShortDto(entity);

        // Then
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getLanguage(), dto.getLanguage());
    }

    @Test
    public void testFromBasicInfoDtoToEntity() {
        // Given
        StudentShortDto dto = new StudentShortDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        // When
        StudentEntity entity = studentMapper.toEntity(dto);

        // Then
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getLanguage(), entity.getLanguage());
        assertFalse(entity.getDeleted());
    }

    @Test
    public void testFromBasicInfoAndEntityToEntity() {
        // Given
        StudentEntity studentEntity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        StudentShortDto dto = new StudentShortDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        // When
        StudentEntity entity = studentMapper.toEntityUpdate(studentEntity, dto);

        // Then
        assertEquals(studentEntity.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getLanguage(), entity.getLanguage());
        assertFalse(entity.getDeleted());
    }
}
