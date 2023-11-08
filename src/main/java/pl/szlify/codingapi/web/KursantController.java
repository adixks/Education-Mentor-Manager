package pl.szlify.codingapi.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.KursantModel;
import pl.szlify.codingapi.service.KursantService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class KursantController {
    private KursantService kursantService;

    public KursantController(KursantService kursantService) {
        this.kursantService = kursantService;
    }

    @GetMapping
    public List<KursantModel> pobierzKursantow() {
        return kursantService.pobierzKursantow();
    }

    @GetMapping("/find/{id}")
    public KursantModel pobierzKursanta(@PathVariable Long id) {
        return kursantService.pobierzKursanta(id);
    }

    @PostMapping
    public KursantModel dodajKursanta(@RequestBody KursantModel kursantModel) {
        return kursantService.dodajKursanta(kursantModel);
    }

    @PatchMapping("/update/{id}")
    public KursantModel aktualizujKursanta(@PathVariable Long id, @RequestBody Long nowyNauczycielId) {
        return kursantService.aktualizujKursanta(id, nowyNauczycielId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> usunKursanta(@PathVariable Long id) {
        kursantService.usunKursanta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
