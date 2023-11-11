package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.szlify.codingapi.model.StudentDto;
import pl.szlify.codingapi.model.StudentBasicInfoDto;
import pl.szlify.codingapi.service.StudentService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentBasicInfoDto> getStudentsList() {
        return studentService.getStudentsList();
    }

    @GetMapping("/{id}")
    public StudentDto getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public StudentBasicInfoDto addStudent(@RequestBody StudentBasicInfoDto studentBasicInfoDto) {
        return studentService.addStudent(studentBasicInfoDto);
    }

    @PutMapping("/{id}")
    public StudentDto updateEntireStudent(@PathVariable Long id, @RequestBody StudentBasicInfoDto studentBasicInfoDto) {
        return studentService.updateEntireStudent(id, studentBasicInfoDto);
    }

    @PatchMapping("/{id}")
    public StudentBasicInfoDto updateStudentTeacher(@PathVariable Long id, @RequestBody Long newTeacherId) {
        return studentService.updateStudentTeacher(id, newTeacherId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
