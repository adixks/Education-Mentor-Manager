package pl.szlify.codingapi.web;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.service.LekcjaService;
import pl.szlify.codingapi.model.LekcjaDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LekcjaController {
    private final LekcjaService lekcjaService;

    @GetMapping
    public List<LekcjaDto> pobierzWszystkieLekcje() {
        return lekcjaService.pobierzWszystkieLekcje();
    }

    @GetMapping("/{id}")
    public LekcjaDto pobierzLekcje(@PathVariable Long id) {
        return lekcjaService.pobierzLekcje(id);
    }

    @PostMapping
    public LekcjaDto stworzLekcje(@RequestBody LekcjaDto lekcjaDto) {
        return lekcjaService.stworzLekcje(lekcjaDto);
    }

    @PutMapping("/{id}")
    public LekcjaDto aktualizujCalaLekcje(@PathVariable Long id, @RequestBody LekcjaDto lekcjaDto) {
        return lekcjaService.aktualizujCalaLekcje(id, lekcjaDto);
    }

    @PatchMapping("/{id}")
    public LekcjaDto aktualizujLekcje(@PathVariable Long id, @RequestBody @NotNull(message = "Data i czas nie moga byc null") LocalDateTime localDateTime) {
        return lekcjaService.aktualizujLekcje(id, localDateTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> usunLekcje(@PathVariable Long id) {
        lekcjaService.usunLekcje(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
