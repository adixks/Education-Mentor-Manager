package pl.szlify.codingapi.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.NauczycielModel;
import pl.szlify.codingapi.service.NauczycielService;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class NauczycielController {
    private NauczycielService nauczycielService;

    public NauczycielController(NauczycielService nauczycielService) {
        this.nauczycielService = nauczycielService;
    }

    @GetMapping
    public List<NauczycielModel> pobierzNauczycieli() {
        return nauczycielService.pobierzNauczycieli();
    }

    @GetMapping("/find/{id}")
    public NauczycielModel pobierzNauczyciela(@PathVariable Long id) {
        return nauczycielService.pobierzNauczyciela(id);
    }

    @PostMapping
    public NauczycielModel dodajNauczyciela(@RequestBody NauczycielModel nauczycielModel) {
        return nauczycielService.dodajNauczyciela(nauczycielModel);
    }

    @PatchMapping("/update/{id}")
    public NauczycielModel aktualizujNauczyciela(@PathVariable Long id, @RequestBody List<String> listaJezykow) {
        return nauczycielService.aktualizujNauczyciela(id, listaJezykow);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> usunNauczyciela(@PathVariable Long id) {
        nauczycielService.usunNauczyciela(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
