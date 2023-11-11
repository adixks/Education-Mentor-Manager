package pl.szlify.codingapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.model.NauczycielEntity;
import pl.szlify.codingapi.exceptions.BrakNauczycielaException;
import pl.szlify.codingapi.mapper.NauczycielMapper;
import pl.szlify.codingapi.model.NauczycieDto;
import pl.szlify.codingapi.model.NauczycielNajwInfoDto;
import pl.szlify.codingapi.repository.NauczycielRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NauczycielEntityServiceTest {

    @Mock
    private NauczycielRepository nauczycielRepository;

    @Mock
    private NauczycielMapper nauczycielMapper;

    @InjectMocks
    private NauczycielService nauczycielService;

    @Test
    void pobierzNauczycieli_shouldReturnListOfNauczycielNajwInfoDto() {
        // Given
        NauczycielEntity nauczyciel1 = new NauczycielEntity();
        NauczycielEntity nauczyciel2 = new NauczycielEntity();
        List<NauczycielEntity> nauczyciele = Arrays.asList(nauczyciel1, nauczyciel2);

        NauczycielNajwInfoDto najwInfoDto1 = new NauczycielNajwInfoDto();
        NauczycielNajwInfoDto najwInfoDto2 = new NauczycielNajwInfoDto();
        List<NauczycielNajwInfoDto> najwInfoDtoList = Arrays.asList(najwInfoDto1, najwInfoDto2);
        when(nauczycielRepository.findAll()).thenReturn(nauczyciele);
        when(nauczycielMapper.fromEntityToNajwInfoDto(nauczyciel1)).thenReturn(najwInfoDto1);
        when(nauczycielMapper.fromEntityToNajwInfoDto(nauczyciel2)).thenReturn(najwInfoDto2);

        // When
        List<NauczycielNajwInfoDto> result = nauczycielService.pobierzNauczycieli();

        // Then
        assertEquals(najwInfoDtoList, result);
        verify(nauczycielRepository, times(1)).findAll();
        verify(nauczycielMapper, times(1)).fromEntityToNajwInfoDto(nauczyciel1);
        verify(nauczycielMapper, times(1)).fromEntityToNajwInfoDto(nauczyciel2);
    }

    @Test
    void testPobierzNauczyciela() {
        // Given
        Long id = 1L;
        NauczycielEntity nauczycielEntity = new NauczycielEntity();
        NauczycieDto nauczycieDto = new NauczycieDto();
        when(nauczycielRepository.findById(id)).thenReturn(Optional.of(nauczycielEntity));
        when(nauczycielMapper.fromEntityToDto(nauczycielEntity)).thenReturn(nauczycieDto);

        // When
        NauczycieDto result = nauczycielService.pobierzNauczyciela(id);

        // Then
        assertNotNull(result);
        assertEquals(nauczycieDto, result);
        verify(nauczycielRepository, times(1)).findById(id);
    }

    @Test
    void testPobierzNauczyciela_Exception() {
        // Given
        Long id = 1L;
        when(nauczycielRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(BrakNauczycielaException.class, () -> nauczycielService.pobierzNauczyciela(id));

        // Then
        verify(nauczycielRepository, times(1)).findById(id);
    }

    @Test
    void testAktualizujCalegoNauczyciela() {
        // Given
        Long nauczycielId = 1L;
        NauczycielNajwInfoDto nauczycielNajwInfoDto = new NauczycielNajwInfoDto();
        NauczycielEntity nauczycielEntity = new NauczycielEntity();
        NauczycielEntity updatedNauczycielEntity = new NauczycielEntity();
        when(nauczycielRepository.findById(nauczycielId)).thenReturn(Optional.of(nauczycielEntity));
        when(nauczycielRepository.save(any(NauczycielEntity.class))).thenReturn(updatedNauczycielEntity);
        when(nauczycielMapper.fromNajwInfoAndEntityToEntity(any(NauczycielEntity.class), eq(nauczycielNajwInfoDto)))
                .thenReturn(updatedNauczycielEntity);
        when(nauczycielMapper.fromEntityToNajwInfoDto(updatedNauczycielEntity)).thenReturn(nauczycielNajwInfoDto);

        // When
        NauczycielNajwInfoDto result = nauczycielService.aktualizujCalegoNauczyciela(nauczycielId, nauczycielNajwInfoDto);

        // Then
        assertEquals(nauczycielNajwInfoDto, result);
        verify(nauczycielRepository, times(1)).findById(nauczycielId);
        verify(nauczycielRepository, times(1)).save(updatedNauczycielEntity);
        verify(nauczycielMapper, times(1)).fromNajwInfoAndEntityToEntity(nauczycielEntity, nauczycielNajwInfoDto);
        verify(nauczycielMapper, times(1)).fromEntityToNajwInfoDto(updatedNauczycielEntity);
    }

    @Test
    void testAktualizujNauczycielaBrakNauczycielaException() {
        // Given
        Long id = 1L;
        List<String> listaJezykow = Arrays.asList("java", "C");
        when(nauczycielRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(BrakNauczycielaException.class, () -> nauczycielService.aktualizujNauczyciela(id, listaJezykow));

        // Then
        verify(nauczycielRepository, times(1)).findById(id);
        verify(nauczycielRepository, never()).save(any(NauczycielEntity.class));
        verify(nauczycielMapper, never()).fromEntityToDto(any(NauczycielEntity.class));
    }

    @Test
    void testUsunNauczyciela() {
        // Given
        Long id = 1L;
        NauczycielEntity nauczycielEntity = new NauczycielEntity();
        nauczycielEntity.setId(id);
        nauczycielEntity.setUsuniety(false);
        when(nauczycielRepository.findById(id)).thenReturn(Optional.of(nauczycielEntity));
        when(nauczycielRepository.save(any(NauczycielEntity.class))).thenReturn(nauczycielEntity);

        // When
        assertDoesNotThrow(() -> nauczycielService.usunNauczyciela(id));

        // Then
        assertTrue(nauczycielEntity.getUsuniety());
        verify(nauczycielRepository, times(1)).findById(id);
        verify(nauczycielRepository, times(1)).save(nauczycielEntity);
    }

    @Test
    void testUsunNauczycielaBrakNauczycielaException() {
        // Given
        Long id = 1L;
        when(nauczycielRepository.findById(id)).thenReturn(Optional.empty());

        // When
        assertThrows(BrakNauczycielaException.class, () -> nauczycielService.usunNauczyciela(id));

        // Then
        verify(nauczycielRepository, times(1)).findById(id);
        verify(nauczycielRepository, never()).save(any(NauczycielEntity.class));
    }
}
