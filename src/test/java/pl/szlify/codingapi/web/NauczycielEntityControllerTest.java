package pl.szlify.codingapi.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.NauczycieDto;
import pl.szlify.codingapi.service.NauczycielService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NauczycielEntityControllerTest {

    @Mock
    private NauczycielService nauczycielService;

    @InjectMocks
    private NauczycielController nauczycielController;

    @Test
    public void testPobierzNauczycieli() {
        // Given
        List<NauczycieDto> nauczyciele = Arrays.asList(new NauczycieDto(), new NauczycieDto());
        when(nauczycielService.pobierzNauczycieli()).thenReturn(nauczyciele);

        // When
        List<NauczycieDto> result = nauczycielController.pobierzNauczycieli();

        // Then
        assertEquals(nauczyciele, result);
    }

    @Test
    public void testPobierzNauczyciela() {
        // Given
        Long id = 1L;
        NauczycieDto nauczyciel = new NauczycieDto();
        when(nauczycielService.pobierzNauczyciela(id)).thenReturn(nauczyciel);

        // When
        NauczycieDto result = nauczycielController.pobierzNauczyciela(id);

        // Then
        assertEquals(nauczyciel, result);
    }

    @Test
    public void testDodajNauczyciela() {
        // Given
        NauczycieDto nauczyciel = new NauczycieDto();
        when(nauczycielService.dodajNauczyciela(nauczyciel)).thenReturn(nauczyciel);

        // When
        NauczycieDto result = nauczycielController.dodajNauczyciela(nauczyciel);

        // Then
        assertEquals(nauczyciel, result);
    }

    @Test
    public void testAktualizujNauczyciela() {
        // Given
        Long id = 1L;
        List<String> listaJezykow = Arrays.asList("java", "c++");
        NauczycieDto nauczyciel = new NauczycieDto();
        when(nauczycielService.aktualizujNauczyciela(id, listaJezykow)).thenReturn(nauczyciel);

        // When
        NauczycieDto result = nauczycielController.aktualizujNauczyciela(id, listaJezykow);

        // Then
        assertEquals(nauczyciel, result);
    }

    @Test
    public void testUsunNauczyciela() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> responseEntity = nauczycielController.usunNauczyciela(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(nauczycielService, times(1)).usunNauczyciela(id);
    }
}
