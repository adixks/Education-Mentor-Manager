package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.KursantModel;
import pl.szlify.codingapi.service.KursantService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KursantControllerTest {

    @Mock
    private KursantService kursantService;

    @InjectMocks
    private KursantController kursantController;

    @Test
    public void testPobierzKursantow() {
        // Given
        List<KursantModel> kursanci = Arrays.asList(new KursantModel(), new KursantModel());
        when(kursantService.pobierzKursantow()).thenReturn(kursanci);

        // When
        List<KursantModel> result = kursantController.pobierzKursantow();

        // Then
        assertEquals(kursanci, result);
    }

    @Test
    public void testPobierzKursanta() {
        // Given
        Long id = 1L;
        KursantModel kursant = new KursantModel();
        when(kursantService.pobierzKursanta(id)).thenReturn(kursant);

        // When
        KursantModel result = kursantController.pobierzKursanta(id);

        // Then
        assertEquals(kursant, result);
    }

    @Test
    public void testDodajKursanta() {
        // Given
        KursantModel kursant = new KursantModel();
        when(kursantService.dodajKursanta(kursant)).thenReturn(kursant);

        // When
        KursantModel result = kursantController.dodajKursanta(kursant);

        // Then
        assertEquals(kursant, result);
    }

    @Test
    public void testAktualizujKursanta() {
        // Given
        Long id = 1L;
        Long nowyNauczycielId = 2L;
        KursantModel kursant = new KursantModel();
        when(kursantService.aktualizujKursanta(id, nowyNauczycielId)).thenReturn(kursant);

        // When
        KursantModel result = kursantController.aktualizujKursanta(id, nowyNauczycielId);

        // Then
        assertEquals(kursant, result);
    }

    @Test
    public void testUsunKursanta() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> responseEntity = kursantController.usunKursanta(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(kursantService, times(1)).usunKursanta(id);
    }
}
