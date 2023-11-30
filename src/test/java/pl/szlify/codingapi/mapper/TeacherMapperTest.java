package pl.szlify.codingapi.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.TeacherEntity;

import java.util.ArrayList;
import java.util.HashSet;

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
                .setDeleted(faker.bool().bool())
                .setLanguages(new HashSet<>(new ArrayList<>()));

        // When
        TeacherFullDto dto = teacherMapper.toFullDto(entity);

        // Then
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
        assertEquals(entity.getDeleted(), dto.getRemoved());
    }

    @Test
    public void testToShortDto() {
        // Given
        TeacherEntity entity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(new HashSet<>(new ArrayList<>()));

        // When
        TeacherShortDto dto = teacherMapper.toShortDto(entity);

        // Then
        assertEquals(entity.getFirstName(), dto.getFirstName());
        assertEquals(entity.getLastName(), dto.getLastName());
    }

    @Test
    public void testToEntity() {
        // Given
        TeacherShortDto dto = new TeacherShortDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(new HashSet<>(new ArrayList<>()));

        // When
        TeacherEntity entity = teacherMapper.toEntity(dto);

        // Then
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertFalse(entity.getDeleted());
        assertTrue(entity.getStudentsList().isEmpty());
    }

    @Test
    public void testToEntityUpdate() {
        // Given
        TeacherEntity teacherEntity = new TeacherEntity()
                .setId(faker.number().randomNumber())
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setDeleted(false)
                .setLanguages(new HashSet<>(new ArrayList<>()));

        TeacherShortDto teacherShortDto = new TeacherShortDto()
                .setFirstName(faker.name().firstName())
                .setLastName(faker.name().lastName())
                .setLanguages(new HashSet<>(new ArrayList<>()));

        // When
        TeacherEntity entity = teacherMapper.toEntityUpdate(teacherEntity, teacherShortDto);

        // Then
        assertEquals(teacherEntity.getId(), entity.getId());
        assertEquals(teacherShortDto.getFirstName(), entity.getFirstName());
        assertEquals(teacherShortDto.getLastName(), entity.getLastName());
        assertFalse(entity.getDeleted());
    }
}
