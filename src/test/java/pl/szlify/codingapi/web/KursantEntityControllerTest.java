package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.KursantDto;
import pl.szlify.codingapi.service.KursantService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KursantEntityControllerTest {

    @Mock
    private KursantService kursantService;

    @InjectMocks
    private KursantController kursantController;

    @Test
    public void testPobierzKursantow() {
        // Given
        List<KursantDto> kursanci = Arrays.asList(new KursantDto(), new KursantDto());
        when(kursantService.pobierzKursantow()).thenReturn(kursanci);

        // When
        List<KursantDto> result = kursantController.pobierzKursantow();

        // Then
        assertEquals(kursanci, result);
    }

    @Test
    public void testPobierzKursanta() {
        // Given
        Long id = 1L;
        KursantDto kursant = new KursantDto();
        when(kursantService.pobierzKursanta(id)).thenReturn(kursant);

        // When
        KursantDto result = kursantController.pobierzKursanta(id);

        // Then
        assertEquals(kursant, result);
    }

    @Test
    public void testDodajKursanta() {
        // Given
        KursantDto kursant = new KursantDto();
        when(kursantService.dodajKursanta(kursant)).thenReturn(kursant);

        // When
        KursantDto result = kursantController.dodajKursanta(kursant);

        // Then
        assertEquals(kursant, result);
    }

    @Test
    public void testAktualizujKursanta() {
        // Given
        Long id = 1L;
        Long nowyNauczycielId = 2L;
        KursantDto kursant = new KursantDto();
        when(kursantService.aktualizujKursanta(id, nowyNauczycielId)).thenReturn(kursant);

        // When
        KursantDto result = kursantController.aktualizujKursanta(id, nowyNauczycielId);

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
