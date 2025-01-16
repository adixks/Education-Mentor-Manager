package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.LessonDateDto;
import pl.szlify.codingapi.service.LessonService;
import pl.szlify.codingapi.model.dto.LessonDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {
    private final LessonService lessonService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping
    public Page<LessonDto> getAllLessons(@PageableDefault(size = 5) Pageable pageable) {
        return lessonService.getList(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping("/{id}")
    public LessonDto getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public LessonDto addLesson(@Valid @RequestBody LessonDto lessonDto) {
        return lessonService.addLesson(lessonDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public LessonDto updateEntireLesson(@PathVariable Long id, @Valid @RequestBody LessonDto lessonDto) {
        return lessonService.updateEntireLesson(id, lessonDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public LessonDto updateLessonDate(@PathVariable Long id, @Valid @RequestBody LessonDateDto lessonDateDto) {
        return lessonService.updateLessonDate(id, lessonDateDto.getDate());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
