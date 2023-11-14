package pl.szlify.codingapi.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.model.TeacherLanguagesDto;
import pl.szlify.codingapi.service.TeacherService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherBasicInfoDto> getTeachersList() {
        return teacherService.getTeachersList();
    }

    @GetMapping("/{id}")
    public TeacherDto getTeacher(@PathVariable Long id) {
        return teacherService.getTeacher(id);
    }

    @PostMapping
    public TeacherDto addTeacher(@Valid @RequestBody TeacherBasicInfoDto teacherBasicInfoDto) {
        return teacherService.addTeacher(teacherBasicInfoDto);
    }

    @PutMapping("/{id}")
    public TeacherBasicInfoDto updateEntireTeacher(@PathVariable Long id,
                                                   @Valid @RequestBody TeacherBasicInfoDto teacherBasicInfoDto) {
        return teacherService.updateEntireTeacher(id, teacherBasicInfoDto);
    }

    @PatchMapping("/{id}")
    public TeacherBasicInfoDto updateTeacherLanguagesList(@PathVariable Long id,
                                                          @Valid @RequestBody TeacherLanguagesDto teacherLanguagesDto) {
        return teacherService.updateTeacherLanguagesList(id, teacherLanguagesDto.getLanguagesList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
