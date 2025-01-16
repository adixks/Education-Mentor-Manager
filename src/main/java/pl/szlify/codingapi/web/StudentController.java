package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.StudentFullDto;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.service.StudentService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping
    public Page<StudentShortDto> getStudentsList(@PageableDefault(size = 5) Pageable pageable) {
        return studentService.getList(pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    @GetMapping("/{id}")
    public StudentFullDto getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public StudentShortDto addStudent(@Valid @RequestBody StudentShortDto studentShortDto) {
        return studentService.addStudent(studentShortDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public StudentFullDto updateEntireStudent(@PathVariable Long id, @Valid @RequestBody StudentShortDto studentShortDto) {
        return studentService.updateEntireStudent(id, studentShortDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public StudentShortDto updateStudentTeacher(@PathVariable Long id, @NotNull @RequestBody Long newTeacherId) {
        return studentService.updateStudentTeacher(id, newTeacherId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
