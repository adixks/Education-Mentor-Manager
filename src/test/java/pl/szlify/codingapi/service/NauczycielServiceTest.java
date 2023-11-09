package pl.szlify.codingapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szlify.codingapi.entity.Nauczyciel;
import pl.szlify.codingapi.exceptions.BrakNauczycielaException;
import pl.szlify.codingapi.mapper.NauczycielMapper;
import pl.szlify.codingapi.model.NauczycielModel;
import pl.szlify.codingapi.repository.NauczycielRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NauczycielServiceTest {

    @Mock
    private NauczycielRepository nauczycielRepository;

    @Mock
    private NauczycielMapper nauczycielMapper;

    @InjectMocks
    private NauczycielService nauczycielService;

    @Test
    void testpobierzNauczycieli() {
        // Given
        when(nauczycielRepository.findAll()).thenReturn(Arrays.asList(new Nauczyciel(), new Nauczyciel()));
        when(nauczycielMapper.from(any(Nauczyciel.class))).thenReturn(new NauczycielModel());

        // When
        List<NauczycielModel> result = nauczycielService.pobierzNauczycieli();

        // Then
        assertEquals(2, result.size());
    }

    @Test
    void testPobierzNauczyciela() {
        // Given
        Long id = 1L;
        Nauczyciel nauczycielEntity = new Nauczyciel();
        NauczycielModel nauczycielModel = new NauczycielModel();
        when(nauczycielRepository.findById(id)).thenReturn(Optional.of(nauczycielEntity));
        when(nauczycielMapper.from(nauczycielEntity)).thenReturn(nauczycielModel);

        // When
        NauczycielModel result = nauczycielService.pobierzNauczyciela(id);

        // Then
        assertNotNull(result);
        assertEquals(nauczycielModel, result);
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
    void shouldAddNauczyciel() {
        // Given
        NauczycielModel nauczycielModel = new NauczycielModel();
        Nauczyciel nauczyciel = new Nauczyciel();
        when(nauczycielMapper.from(nauczycielModel)).thenReturn(nauczyciel);
        when(nauczycielRepository.save(nauczyciel)).thenReturn(nauczyciel);
        when(nauczycielMapper.from(nauczyciel)).thenReturn(new NauczycielModel());

        // When
        NauczycielModel result = nauczycielService.dodajNauczyciela(nauczycielModel);

        // Then
        assertEquals(new NauczycielModel(), result);
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
        verify(nauczycielRepository, never()).save(any(Nauczyciel.class));
        verify(nauczycielMapper, never()).from(any(Nauczyciel.class));
    }

    @Test
    void testUsunNauczyciela() {
        // Given
        Long id = 1L;
        Nauczyciel nauczycielEntity = new Nauczyciel();
        nauczycielEntity.setId(id);
        nauczycielEntity.setUsuniety(false);
        when(nauczycielRepository.findById(id)).thenReturn(Optional.of(nauczycielEntity));
        when(nauczycielRepository.save(any(Nauczyciel.class))).thenReturn(nauczycielEntity);

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
        verify(nauczycielRepository, never()).save(any(Nauczyciel.class));
    }
}
