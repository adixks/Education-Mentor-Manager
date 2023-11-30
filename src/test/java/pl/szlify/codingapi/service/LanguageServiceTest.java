package pl.szlify.codingapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.szlify.codingapi.mapper.LanguageMapper;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.repository.LanguageRepository;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class LanguageServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private LanguageMapper languageMapper;

    @InjectMocks
    private LanguageService languageService;

    private final Faker faker = new Faker();

    @Test
    void testGetList() {
        // Given
        Pageable pageable = mock(Pageable.class);
        LanguageEntity languageEntity = new LanguageEntity();
        LanguageShortDto languageShortDto = new LanguageShortDto();

        Page<LanguageEntity> languageEntitiesPage = new PageImpl<>(Collections.singletonList(languageEntity));

        when(languageRepository.findAll(pageable)).thenReturn(languageEntitiesPage);
        when(languageMapper.toShortDto(languageEntity)).thenReturn(languageShortDto);

        // When
        Page<LanguageShortDto> result = languageService.getList(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        assertEquals(languageShortDto, result.getContent().get(0));
    }

    @Test
    void testAddLanguage() {
        // Given
        LanguageShortDto languageShortDto = new LanguageShortDto();
        LanguageEntity languageEntity = new LanguageEntity();

        when(languageMapper.toEntity(languageShortDto)).thenReturn(languageEntity);
        when(languageRepository.save(languageEntity)).thenReturn(languageEntity);
        when(languageMapper.toShortDto(languageEntity)).thenReturn(languageShortDto);

        // When
        LanguageShortDto result = languageService.addLanguage(languageShortDto);

        // Then
        assertEquals(languageShortDto, result);
    }

    @Test
    void testDeleteLanguage() {
        // Given
        Long languageId = faker.number().randomNumber();

        // When
        languageService.deleteLanguage(languageId);

        // Then
        verify(languageRepository, times(1)).deleteById(languageId);
    }
}
