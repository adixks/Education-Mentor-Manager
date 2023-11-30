package pl.szlify.codingapi.mapper;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.dto.LanguageShortDto;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LanguageMapperTest {

    @Mock
    private LanguageEntity entity;

    @Mock
    private LanguageShortDto model;

    @InjectMocks
    private LanguageMapper languageMapper;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        faker = new Faker();
    }

    @Test
    public void testToShortDto() {
        // Given
        LanguageShortDto expected = new LanguageShortDto()
                .setId(entity.getId())
                .setName(entity.getName());

        // When
        LanguageShortDto languageShortDto = languageMapper.toShortDto(entity);

        // Then
        assertEquals(expected.getId(), languageShortDto.getId());
        assertEquals(expected.getName(), languageShortDto.getName());
    }

    @Test
    public void testToEntity() {
        // Given
        LanguageEntity expected = new LanguageEntity()
                .setId(model.getId())
                .setName(model.getName())
                .setStudents(new HashSet<>())
                .setTeachers(new HashSet<>());

        // When
        LanguageEntity languageEntity = languageMapper.toEntity(model);

        // Then
        assertEquals(expected.getId(), languageEntity.getId());
        assertEquals(expected.getName(), languageEntity.getName());
    }
}
