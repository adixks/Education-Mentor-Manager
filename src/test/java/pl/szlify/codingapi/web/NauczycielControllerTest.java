package pl.szlify.codingapi.web;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.NauczycielModel;
import pl.szlify.codingapi.service.NauczycielService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NauczycielControllerTest {

    @Mock
    private NauczycielService nauczycielService;

    @InjectMocks
    private NauczycielController nauczycielController;

    @Test
    public void testPobierzNauczycieli() {
        // Given
        List<NauczycielModel> nauczyciele = Arrays.asList(new NauczycielModel(), new NauczycielModel());
        when(nauczycielService.pobierzNauczycieli()).thenReturn(nauczyciele);

        // When
        List<NauczycielModel> result = nauczycielController.pobierzNauczycieli();

        // Then
        assertEquals(nauczyciele, result);
    }

    @Test
    public void testPobierzNauczyciela() {
        // Given
        Long id = 1L;
        NauczycielModel nauczyciel = new NauczycielModel();
        when(nauczycielService.pobierzNauczyciela(id)).thenReturn(nauczyciel);

        // When
        NauczycielModel result = nauczycielController.pobierzNauczyciela(id);

        // Then
        assertEquals(nauczyciel, result);
    }

    @Test
    public void testDodajNauczyciela() {
        // Given
        NauczycielModel nauczyciel = new NauczycielModel();
        when(nauczycielService.dodajNauczyciela(nauczyciel)).thenReturn(nauczyciel);

        // When
        NauczycielModel result = nauczycielController.dodajNauczyciela(nauczyciel);

        // Then
        assertEquals(nauczyciel, result);
    }

    @Test
    public void testAktualizujNauczyciela() {
        // Given
        Long id = 1L;
        List<String> listaJezykow = Arrays.asList("java", "c++");
        NauczycielModel nauczyciel = new NauczycielModel();
        when(nauczycielService.aktualizujNauczyciela(id, listaJezykow)).thenReturn(nauczyciel);

        // When
        NauczycielModel result = nauczycielController.aktualizujNauczyciela(id, listaJezykow);

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
