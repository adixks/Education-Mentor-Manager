package pl.szlify.codingapi.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.szlify.codingapi.model.dto.StudentRegistrationDto;
import pl.szlify.codingapi.model.dto.TeacherRegistrationDto;
import pl.szlify.codingapi.service.StudentService;
import pl.szlify.codingapi.service.TeacherService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final StudentService studentService;
    private final TeacherService teacherService;

    @PostMapping("/register/student")
    public ResponseEntity<String> registerStudent(@Valid @RequestBody StudentRegistrationDto studentDto) {
        studentService.registerStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Student registered successfully!");
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<String> registerTeacher(@Valid @RequestBody TeacherRegistrationDto teacherDto) {
        teacherService.registerTeacher(teacherDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Teacher registered successfully!");
    }
}
