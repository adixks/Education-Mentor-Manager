package pl.szlify.codingapi.mapper;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import pl.szlify.codingapi.model.StudentBasicInfoDto;
import pl.szlify.codingapi.model.StudentDto;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;

import static org.junit.jupiter.api.Assertions.*;

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
                .setRemoved(faker.bool().bool());

        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber());

        entity.setTeacherEntity(teacherEntity);

        // When
        StudentDto dto = studentMapper.fromEntityToDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getLanguage(), dto.getLanguage());
        assertEquals(entity.getRemoved(), dto.getRemoved());
        assertEquals(entity.getTeacherEntity().getId(), dto.getTeacherId());
    }

    @Test
    public void testFromDtoToEntity() {
        // Given
        StudentDto model = new StudentDto()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word())
                .setRemoved(faker.bool().bool());

        // When
        StudentEntity entity = studentMapper.fromDtoToEntity(model);

        // Then
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getFirstName(), entity.getFirstName());
        assertEquals(model.getLastName(), entity.getLastName());
        assertEquals(model.getLanguage(), entity.getLanguage());
        assertEquals(model.getRemoved(), entity.getRemoved());
    }

    @Test
    public void testFromDtoAndEntityToEntity() {
        // Given
        StudentEntity studentEntity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word())
                .setRemoved(faker.bool().bool());

        StudentDto studentDto = new StudentDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word())
                .setRemoved(faker.bool().bool());

        // When
        StudentEntity entity = studentMapper.fromDtoAndEntityToEntity(studentEntity, studentDto);

        // Then
        assertEquals(studentEntity.getId(), entity.getId());
        assertEquals(studentDto.getFirstName(), entity.getFirstName());
        assertEquals(studentDto.getLastName(), entity.getLastName());
        assertEquals(studentDto.getLanguage(), entity.getLanguage());
        assertEquals(studentDto.getRemoved(), entity.getRemoved());
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

        entity.setTeacherEntity(teacherEntity);

        // When
        StudentBasicInfoDto dto = studentMapper.fromEntityToBasicInfoDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getLanguage(), dto.getLanguage());
        assertEquals(entity.getTeacherEntity().getId(), dto.getTeacherId());
    }

    @Test
    public void testFromBasicInfoDtoToEntity() {
        // Given
        StudentBasicInfoDto dto = new StudentBasicInfoDto()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        // When
        StudentEntity entity = studentMapper.fromBasicInfoDtoToEntity(dto);

        // Then
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getLanguage(), entity.getLanguage());
        assertFalse(entity.getRemoved());
    }

    @Test
    public void testFromBasicInfoAndEntityToEntity() {
        // Given
        StudentEntity studentEntity = new StudentEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        StudentBasicInfoDto dto = new StudentBasicInfoDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguage(faker.lorem().word());

        // When
        StudentEntity entity = studentMapper.fromBasicInfoAndEntityToEntity(studentEntity, dto);

        // Then
        assertEquals(studentEntity.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getLanguage(), entity.getLanguage());
        assertFalse(entity.getRemoved());
    }
}
