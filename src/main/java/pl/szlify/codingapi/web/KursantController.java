package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.KursantDto;
import pl.szlify.codingapi.model.KursantNajwInfoDto;
import pl.szlify.codingapi.service.KursantService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/students")
public class KursantController {
    private final KursantService kursantService;

    @GetMapping
    public List<KursantNajwInfoDto> pobierzKursantow() {
        return kursantService.pobierzKursantow();
    }

    @GetMapping("/{id}")
    public KursantDto pobierzKursanta(@PathVariable Long id) {
        return kursantService.pobierzKursanta(id);
    }

    @PostMapping
    public KursantNajwInfoDto dodajKursanta(@RequestBody KursantNajwInfoDto kursantNajwInfoDto) {
        return kursantService.dodajKursanta(kursantNajwInfoDto);
    }

    @PutMapping("/{id}")
    public KursantDto aktualizujCalegoKursanta(@PathVariable Long id, @RequestBody KursantNajwInfoDto kursantNajwInfoDto) {
        return kursantService.aktualizujCalegoKursanta(id, kursantNajwInfoDto);
    }

    @PatchMapping("/{id}")
    public KursantNajwInfoDto aktualizujKursanta(@PathVariable Long id, @RequestBody Long nowyNauczycielId) {
        return kursantService.aktualizujKursanta(id, nowyNauczycielId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> usunKursanta(@PathVariable Long id) {
        kursantService.usunKursanta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
