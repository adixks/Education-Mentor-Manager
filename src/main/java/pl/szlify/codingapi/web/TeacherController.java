package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.model.dto.TeacherLanguagesDto;
import pl.szlify.codingapi.service.TeacherService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
    @GetMapping
    public Page<TeacherShortDto> getTeachersList(@PageableDefault(size = 5) Pageable pageable) {
        return teacherService.getList(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/{id}")
    public TeacherFullDto getTeacher(@PathVariable Long id) {
        return teacherService.getTeacher(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public TeacherFullDto addTeacher(@Valid @RequestBody TeacherShortDto teacherShortDto) {
        return teacherService.addTeacher(teacherShortDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public TeacherShortDto updateEntireTeacher(@PathVariable Long id, @Valid @RequestBody TeacherShortDto teacherShortDto) {
        return teacherService.updateEntireTeacher(id, teacherShortDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public TeacherShortDto updateTeacherLanguagesList(@PathVariable Long id, @Valid @RequestBody TeacherLanguagesDto teacherLanguagesDto) {
        return teacherService.updateTeacherLanguagesList(id, teacherLanguagesDto.getLanguagesList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
