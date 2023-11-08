package pl.szlify.codingapi.web;

import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.service.LekcjaService;
import pl.szlify.codingapi.model.LekcjaModel;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LekcjaController {
    private LekcjaService lekcjaService;

    public LekcjaController(LekcjaService lekcjaService) {
        this.lekcjaService = lekcjaService;
    }

    @GetMapping
    public List<LekcjaModel> pobierzWszystkieLekcje() {
        return lekcjaService.pobierzWszystkieLekcje();
    }

    @GetMapping("/find/{id}")
    public LekcjaModel pobierzLekcje(@PathVariable Long id) {
        return lekcjaService.pobierzLekcje(id);
    }

    @PostMapping
    public LekcjaModel stworzLekcje(@RequestBody LekcjaModel lekcjaModel) {
        return lekcjaService.stworzLekcje(lekcjaModel);
    }

    @PatchMapping("/update/{id}")
    public LekcjaModel aktualizujLekcje(@PathVariable Long id, @RequestBody @NotNull(message = "Data i czas nie moga byc null") LocalDateTime localDateTime) {
        return lekcjaService.aktualizujLekcje(id, localDateTime);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> usunLekcje(@PathVariable Long id) {
        lekcjaService.usunLekcje(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
