package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.NauczycieDto;
import pl.szlify.codingapi.model.NauczycielNajwInfoDto;
import pl.szlify.codingapi.service.NauczycielService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/teachers")
public class NauczycielController {
    private final NauczycielService nauczycielService;

    @GetMapping
    public List<NauczycielNajwInfoDto> pobierzNauczycieli() {
        return nauczycielService.pobierzNauczycieli();
    }

    @GetMapping("/{id}")
    public NauczycieDto pobierzNauczyciela(@PathVariable Long id) {
        return nauczycielService.pobierzNauczyciela(id);
    }

    @PostMapping
    public NauczycieDto dodajNauczyciela(@RequestBody NauczycielNajwInfoDto nauczycielNajwInfoDto) {
        return nauczycielService.dodajNauczyciela(nauczycielNajwInfoDto);
    }

    @PutMapping("/{id}")
    public NauczycielNajwInfoDto aktualizujCalegoNauczyciela(@PathVariable Long id, @RequestBody NauczycielNajwInfoDto nauczycielNajwInfoDto) {
        return nauczycielService.aktualizujCalegoNauczyciela(id, nauczycielNajwInfoDto);
    }

    @PatchMapping("/{id}")
    public NauczycielNajwInfoDto aktualizujNauczyciela(@PathVariable Long id, @RequestBody List<String> listaJezykow) {
        return nauczycielService.aktualizujNauczyciela(id, listaJezykow);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> usunNauczyciela(@PathVariable Long id) {
        nauczycielService.usunNauczyciela(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
