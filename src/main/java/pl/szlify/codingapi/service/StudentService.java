package pl.szlify.codingapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.exceptions.MissingStudentException;
import pl.szlify.codingapi.exceptions.LackofTeacherException;
import pl.szlify.codingapi.exceptions.BadLanguageException;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;
import pl.szlify.codingapi.mapper.StudentMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;

    public List<StudentBasicInfoDto> getStudentsList() {
        return studentRepository.findAll().stream()
                .map(studentMapper::fromEntityToBasicInfoDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudent(Long id) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(MissingStudentException::new);
        return studentMapper.fromEntityToDto(studentEntity);
    }

    public StudentBasicInfoDto addStudent(StudentBasicInfoDto studentBasicInfoDto) {
        TeacherEntity teacherEntity = teacherRepository.findByIdAndRemovedFalse(studentBasicInfoDto.getTeacherId())
                .orElseThrow(LackofTeacherException::new);
        if (!teacherEntity.getLanguages().contains(studentBasicInfoDto.getLanguage())) {
            throw new BadLanguageException();
        }
        StudentEntity studentEntity = studentMapper.fromBasicInfoDtoToEntity(studentBasicInfoDto);
        studentEntity.setTeacherEntity(teacherEntity);
        return studentMapper.fromEntityToBasicInfoDto(studentRepository.save(studentEntity));
    }

    public StudentDto updateEntireStudent(Long id, StudentBasicInfoDto studentBasicInfoDto) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(MissingStudentException::new);

        TeacherEntity teacherEntity = teacherRepository.findByIdAndRemovedFalse(studentBasicInfoDto.getTeacherId())
                .orElseThrow(LackofTeacherException::new);

        if (!teacherEntity.getLanguages().contains(studentBasicInfoDto.getLanguage())) {
            throw new BadLanguageException();
        }

        StudentEntity updatedStudentEntity = studentMapper
                .fromBasicInfoAndEntityToEntity(studentEntity, studentBasicInfoDto);
        updatedStudentEntity.setTeacherEntity(teacherEntity);
        return studentMapper.fromEntityToDto(studentRepository.save(updatedStudentEntity));
    }

    public StudentBasicInfoDto updateStudentTeacher(Long id, Long teacherId) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(MissingStudentException::new);

        TeacherEntity teacherEntity = teacherRepository.findByIdAndRemovedFalse(teacherId)
                .orElseThrow(LackofTeacherException::new);

        if (!teacherEntity.getLanguages().contains(studentEntity.getLanguage())) {
            throw new BadLanguageException();
        }
        studentEntity.setTeacherEntity(teacherEntity);
        studentRepository.save(studentEntity);
        return studentMapper.fromEntityToBasicInfoDto(studentEntity);
    }

    public void deleteStudent(Long id) {
        Optional<LessonEntity> existingLesson = lessonRepository.findByStudentEntityId(id);
        if (existingLesson.isPresent()) {
            throw new LessonInFutureException();
        }
        studentRepository.deleteById(id);
    }
}
