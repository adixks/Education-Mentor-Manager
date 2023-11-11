package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.LekcjaDto;
import pl.szlify.codingapi.service.LekcjaService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LekcjaEntityControllerTest {

    @Mock
    private LekcjaService lekcjaService;

    @InjectMocks
    private LekcjaController lekcjaController;

    @Test
    public void testPobierzWszystkieLekcje() {
        // Given
        List<LekcjaDto> lekcje = Arrays.asList(new LekcjaDto(), new LekcjaDto());
        when(lekcjaService.pobierzWszystkieLekcje()).thenReturn(lekcje);

        // When
        List<LekcjaDto> result = lekcjaController.pobierzWszystkieLekcje();

        // Then
        assertEquals(lekcje, result);
    }

    @Test
    public void testPobierzLekcje() {
        // Given
        Long id = 1L;
        LekcjaDto lekcja = new LekcjaDto();
        when(lekcjaService.pobierzLekcje(id)).thenReturn(lekcja);

        // When
        LekcjaDto result = lekcjaController.pobierzLekcje(id);

        // Then
        assertEquals(lekcja, result);
    }

    @Test
    public void testStworzLekcje() {
        // Given
        LekcjaDto lekcja = new LekcjaDto();
        when(lekcjaService.stworzLekcje(lekcja)).thenReturn(lekcja);

        // When
        LekcjaDto result = lekcjaController.stworzLekcje(lekcja);

        // Then
        assertEquals(lekcja, result);
    }

    @Test
    public void testAktualizujLekcje() {
        // Given
        Long id = 1L;
        LocalDateTime localDateTime = LocalDateTime.now();
        LekcjaDto lekcja = new LekcjaDto();
        when(lekcjaService.aktualizujLekcje(id, localDateTime)).thenReturn(lekcja);

        // When
        LekcjaDto result = lekcjaController.aktualizujLekcje(id, localDateTime);

        // Then
        assertEquals(lekcja, result);
    }

    @Test
    public void testUsunLekcje() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> responseEntity = lekcjaController.usunLekcje(id);

        // When
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(lekcjaService, times(1)).usunLekcje(id);
    }
}
