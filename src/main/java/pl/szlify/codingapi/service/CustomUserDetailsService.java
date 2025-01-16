package pl.szlify.codingapi.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public CustomUserDetailsService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to authenticate user: " + username);

        Optional<StudentEntity> student = studentRepository.findByUsername(username);
        if (student.isPresent()) {
            StudentEntity studentEntity = student.get();
            System.out.println("Found student: " + studentEntity.getUsername());
            return User.builder()
                    .username(studentEntity.getUsername())
                    .password(studentEntity.getPassword())
                    .roles("STUDENT")
                    .build();
        }

        Optional<TeacherEntity> teacher = teacherRepository.findByUsername(username);
        if (teacher.isPresent()) {
            TeacherEntity teacherEntity = teacher.get();
            System.out.println("Found teacher: " + teacherEntity.getUsername());
            return User.builder()
                    .username(teacherEntity.getUsername())
                    .password(teacherEntity.getPassword())
                    .roles("TEACHER")
                    .build();
        }

        System.out.println("User not found: " + username);
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
