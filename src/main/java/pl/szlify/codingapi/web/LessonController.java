package pl.szlify.codingapi.web;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.service.LessonService;
import pl.szlify.codingapi.model.LessonDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/lessons")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping
    public List<LessonDto> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public LessonDto getLesson(@PathVariable Long id) {
        return lessonService.getLesson(id);
    }

    @PostMapping
    public LessonDto addLesson(@RequestBody LessonDto lessonDto) {
        return lessonService.addLesson(lessonDto);
    }

    @PutMapping("/{id}")
    public LessonDto updateEntireLesson(@PathVariable Long id, @RequestBody LessonDto lessonDto) {
        return lessonService.updateEntireLesson(id, lessonDto);
    }

    @PatchMapping("/{id}")
    public LessonDto updateLessonDate(@PathVariable Long id, @RequestBody @NotNull(message = "Date and time must not be null") LocalDateTime localDateTime) {
        return lessonService.updateLessonDate(id, localDateTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
