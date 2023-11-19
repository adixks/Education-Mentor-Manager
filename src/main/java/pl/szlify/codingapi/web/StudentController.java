package pl.szlify.codingapi.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.dto.StudentFullDto;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.service.StudentService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentShortDto> getStudentsList() {
        return studentService.getStudentsList();
    }

    @GetMapping("/{id}")
    public StudentFullDto getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public StudentShortDto addStudent(@Valid @RequestBody StudentShortDto studentShortDto) {
        return studentService.addStudent(studentShortDto);
    }

    @PutMapping("/{id}")
    public StudentFullDto updateEntireStudent(@PathVariable Long id,
                                              @Valid @RequestBody StudentShortDto studentShortDto) {
        return studentService.updateEntireStudent(id, studentShortDto);
    }

    @PatchMapping("/{id}")
    public StudentShortDto updateStudentTeacher(@PathVariable Long id,
                                                @NotNull @RequestBody Long newTeacherId) {
        return studentService.updateStudentTeacher(id, newTeacherId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
