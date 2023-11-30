package pl.szlify.codingapi.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.model.dto.LessonDto;
import pl.szlify.codingapi.service.LanguageService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/language")
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping
    public Page<LanguageShortDto> getAllLessons(@PageableDefault(size = 5) Pageable pageable) {
        return languageService.getList(pageable);
    }

    @PostMapping
    public LanguageShortDto addLanguage(@Valid @RequestBody LanguageShortDto languageShortDto) {
        return languageService.addLanguage(languageShortDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
