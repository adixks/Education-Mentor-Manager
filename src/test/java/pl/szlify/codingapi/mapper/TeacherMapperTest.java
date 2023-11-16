package pl.szlify.codingapi.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TeacherMapperTest {

    private Faker faker;

    @InjectMocks
    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        faker = new Faker();
    }

    @Test
    public void testFromEntityToDto() {
        // Given
        TeacherEntity entity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setRemoved(faker.bool().bool())
                .setLanguages(faker.lorem().words());

        // When
        TeacherDto dto = teacherMapper.fromEntityToDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getRemoved(), dto.getRemoved());
        assertEquals(entity.getLanguages(), dto.getLanguages());
    }

    @Test
    public void testFromEntityToNajwInfoDto() {
        // Given
        TeacherEntity entity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(faker.lorem().words());

        // When
        TeacherBasicInfoDto dto = teacherMapper.fromEntityToNajwInfoDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getLanguages(), dto.getLanguages());
    }

    @Test
    public void testFromNajwInfoToEntity() {
        // Given
        TeacherBasicInfoDto dto = new TeacherBasicInfoDto()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(faker.lorem().words());

        // When
        TeacherEntity entity = teacherMapper.fromNajwInfoToEntity(dto);

        // Then
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getLanguages(), entity.getLanguages());
        assertFalse(entity.getRemoved());
        assertTrue(entity.getStudentsList().isEmpty());
    }

    @Test
    public void testFromNajwInfoAndEntityToEntity() {
        // Given
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(faker.lorem().words());

        TeacherBasicInfoDto teacherBasicInfoDto = new TeacherBasicInfoDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(faker.lorem().words());

        // When
        TeacherEntity entity = teacherMapper.fromNajwInfoAndEntityToEntity(teacherEntity, teacherBasicInfoDto);

        // Then
        assertEquals(teacherEntity.getId(), entity.getId());
        assertEquals(teacherBasicInfoDto.getFirstName(), entity.getFirstName());
        assertEquals(teacherBasicInfoDto.getLastName(), entity.getLastName());
        assertEquals(teacherBasicInfoDto.getLanguages(), entity.getLanguages());
        assertFalse(entity.getRemoved());
        assertTrue(entity.getStudentsList().isEmpty());
    }

    @Test
    public void testFromDtoToEntity() {
        // Given
        TeacherDto model = new TeacherDto()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setRemoved(faker.bool().bool())
                .setLanguages(faker.lorem().words());

        // When
        TeacherEntity entity = teacherMapper.fromDtoToEntity(model);

        // Then
        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getFirstName(), entity.getFirstName());
        assertEquals(model.getLastName(), entity.getLastName());
        assertEquals(model.getRemoved(), entity.getRemoved());
        assertEquals(model.getLanguages(), entity.getLanguages());
    }
}
