package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.service.LanguageService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/language")
public class LanguageController {
    private final LanguageService languageService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping
    public Page<LanguageShortDto> getAllLanguage(@PageableDefault(size = 5) Pageable pageable) {
        return languageService.getList(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public LanguageShortDto addLanguage(@Valid @RequestBody LanguageShortDto languageShortDto) {
        return languageService.addLanguage(languageShortDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
