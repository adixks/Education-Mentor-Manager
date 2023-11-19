package pl.szlify.codingapi.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.model.dto.TeacherLanguagesDto;
import pl.szlify.codingapi.service.TeacherService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherShortDto> getTeachersList() {
        return teacherService.getTeachersList();
    }

    @GetMapping("/{id}")
    public TeacherFullDto getTeacher(@PathVariable Long id) {
        return teacherService.getTeacher(id);
    }

    @PostMapping
    public TeacherFullDto addTeacher(@Valid @RequestBody TeacherShortDto teacherShortDto) {
        return teacherService.addTeacher(teacherShortDto);
    }

    @PutMapping("/{id}")
    public TeacherShortDto updateEntireTeacher(@PathVariable Long id,
                                               @Valid @RequestBody TeacherShortDto teacherShortDto) {
        return teacherService.updateEntireTeacher(id, teacherShortDto);
    }

    @PatchMapping("/{id}")
    public TeacherShortDto updateTeacherLanguagesList(@PathVariable Long id,
                                                      @Valid @RequestBody TeacherLanguagesDto teacherLanguagesDto) {
        return teacherService.updateTeacherLanguagesList(id, teacherLanguagesDto.getLanguagesList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
