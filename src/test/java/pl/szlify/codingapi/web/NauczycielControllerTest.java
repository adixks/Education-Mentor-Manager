package pl.szlify.codingapi.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szlify.codingapi.model.NauczycieDto;
import pl.szlify.codingapi.model.NauczycielNajwInfoDto;
import pl.szlify.codingapi.service.NauczycielService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NauczycielControllerTest {

    @Mock
    private NauczycielService nauczycielService;

    @InjectMocks
    private NauczycielController nauczycielController;

    @Test
    void testPobierzNauczycieli() {
        // Given
        when(nauczycielService.pobierzNauczycieli()).thenReturn(Arrays.asList(new NauczycielNajwInfoDto(), new NauczycielNajwInfoDto()));

        // When
        List<NauczycielNajwInfoDto> result = nauczycielController.pobierzNauczycieli();

        // Then
        assertEquals(2, result.size());
        verify(nauczycielService, times(1)).pobierzNauczycieli();
    }

    @Test
    void testPobierzNauczyciela() {
        // Given
        Long id = 1L;
        when(nauczycielService.pobierzNauczyciela(id)).thenReturn(new NauczycieDto());

        // When
        NauczycieDto result = nauczycielController.pobierzNauczyciela(id);

        // Then
        assertEquals(NauczycieDto.class, result.getClass());
        verify(nauczycielService, times(1)).pobierzNauczyciela(id);
    }

    @Test
    void testDodajNauczyciela() {
        // Given
        NauczycielNajwInfoDto nauczycielDto = new NauczycielNajwInfoDto();
        when(nauczycielService.dodajNauczyciela(nauczycielDto)).thenReturn(new NauczycieDto());

        // When
        NauczycieDto result = nauczycielController.dodajNauczyciela(nauczycielDto);

        // Then
        assertEquals(NauczycieDto.class, result.getClass());
        verify(nauczycielService, times(1)).dodajNauczyciela(nauczycielDto);
    }

    @Test
    void testAktualizujCalegoNauczyciela() {
        // Given
        Long id = 1L;
        NauczycielNajwInfoDto nauczycielDto = new NauczycielNajwInfoDto();
        when(nauczycielService.aktualizujCalegoNauczyciela(id, nauczycielDto)).thenReturn(nauczycielDto);

        // When
        NauczycielNajwInfoDto result = nauczycielController.aktualizujCalegoNauczyciela(id, nauczycielDto);

        // Then
        assertEquals(NauczycielNajwInfoDto.class, result.getClass());
        verify(nauczycielService, times(1)).aktualizujCalegoNauczyciela(id, nauczycielDto);
    }

    @Test
    void testAktualizujNauczyciela() {
        // Given
        Long id = 1L;
        List<String> listaJezykow = Arrays.asList("jezyk1", "jezyk2");
        NauczycielNajwInfoDto nauczycielDto = new NauczycielNajwInfoDto();
        when(nauczycielService.aktualizujNauczyciela(id, listaJezykow)).thenReturn(nauczycielDto);

        // When
        NauczycielNajwInfoDto result = nauczycielController.aktualizujNauczyciela(id, listaJezykow);

        // Then
        assertEquals(NauczycielNajwInfoDto.class, result.getClass());
        verify(nauczycielService, times(1)).aktualizujNauczyciela(id, listaJezykow);
    }

    @Test
    void testUsunNauczyciela() {
        // Given
        Long id = 1L;

        // When
        ResponseEntity<Void> result = nauczycielController.usunNauczyciela(id);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(nauczycielService, times(1)).usunNauczyciela(id);
    }
}
