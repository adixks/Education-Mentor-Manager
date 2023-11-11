package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.KursantDto;
import pl.szlify.codingapi.model.KursantNajwInfoDto;
import pl.szlify.codingapi.service.KursantService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KursantControllerTest {

    @Mock
    private KursantService kursantService;

    @InjectMocks
    private KursantController kursantController;

    @Test
    void testPobierzKursantow() {
        // Given
        List<KursantNajwInfoDto> kursantList = Arrays.asList(new KursantNajwInfoDto(), new KursantNajwInfoDto());
        when(kursantService.pobierzKursantow()).thenReturn(kursantList);

        // When
        List<KursantNajwInfoDto> result = kursantController.pobierzKursantow();

        // Then
        assertEquals(kursantList, result);
    }

    @Test
    void testPobierzKursanta() {
        // Given
        Long kursantId = 1L;
        KursantDto kursantDto = new KursantDto();
        when(kursantService.pobierzKursanta(kursantId)).thenReturn(kursantDto);

        // When
        KursantDto result = kursantController.pobierzKursanta(kursantId);

        // Then
        assertEquals(kursantDto, result);
    }

    @Test
    void testDodajKursanta() {
        // Given
        KursantNajwInfoDto kursantNajwInfoDto = new KursantNajwInfoDto();
        when(kursantService.dodajKursanta(kursantNajwInfoDto)).thenReturn(kursantNajwInfoDto);

        // When
        KursantNajwInfoDto result = kursantController.dodajKursanta(kursantNajwInfoDto);

        // Then
        assertEquals(kursantNajwInfoDto, result);
    }

    @Test
    void testAktualizujCalegoKursanta() {
        // Given
        Long kursantId = 1L;
        KursantNajwInfoDto kursantNajwInfoDto = new KursantNajwInfoDto();
        KursantDto kursantDto = new KursantDto();
        when(kursantService.aktualizujCalegoKursanta(kursantId, kursantNajwInfoDto)).thenReturn(kursantDto);

        // When
        KursantDto result = kursantController.aktualizujCalegoKursanta(kursantId, kursantNajwInfoDto);

        // Then
        assertEquals(kursantDto, result);
    }

    @Test
    void testAktualizujKursanta() {
        // Given
        Long kursantId = 1L;
        Long nowyNauczycielId = 2L;
        KursantNajwInfoDto kursantNajwInfoDto = new KursantNajwInfoDto();
        when(kursantService.aktualizujKursanta(kursantId, nowyNauczycielId)).thenReturn(kursantNajwInfoDto);

        // When
        KursantNajwInfoDto result = kursantController.aktualizujKursanta(kursantId, nowyNauczycielId);

        // Then
        assertEquals(kursantNajwInfoDto, result);
    }

    @Test
    void testUsunKursanta() {
        // Given
        Long kursantId = 1L;

        // When
        ResponseEntity<Void> result = kursantController.usunKursanta(kursantId);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(kursantService, times(1)).usunKursanta(kursantId);
    }
}
