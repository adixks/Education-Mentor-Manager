package pl.szlify.codingapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.exceptions.MissingStudentException;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.exceptions.BadLanguageException;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.model.dto.StudentFullDto;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.TeacherRepository;
import pl.szlify.codingapi.mapper.StudentMapper;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final StudentMapper studentMapper;

    public Page<StudentShortDto> getStudentsList(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toShortDto);
    }

    public StudentFullDto getStudent(Long id) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(MissingStudentException::new);
        return studentMapper.toFullDto(studentEntity);
    }

    public StudentShortDto addStudent(StudentShortDto studentShortDto) {
        TeacherEntity teacherEntity = teacherRepository.findByIdAndDeletedFalse(studentShortDto.getTeacherId())
                .orElseThrow(LackOfTeacherException::new);
        if (!teacherEntity.getLanguages().contains(studentShortDto.getLanguage())) {
            throw new BadLanguageException();
        }
        StudentEntity studentEntity = studentMapper.toEntity(studentShortDto);
        studentEntity.setTeacher(teacherEntity);
        return studentMapper.toShortDto(studentRepository.save(studentEntity));
    }

    @Transactional
    public StudentFullDto updateEntireStudent(Long id, StudentShortDto studentShortDto) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(MissingStudentException::new);

        TeacherEntity teacherEntity = teacherRepository.findByIdAndDeletedFalse(studentShortDto.getTeacherId())
                .orElseThrow(LackOfTeacherException::new);

        if (!teacherEntity.getLanguages().contains(studentShortDto.getLanguage())) {
            throw new BadLanguageException();
        }

        StudentEntity updatedStudentEntity = studentMapper
                .toEntityUpdate(studentEntity, studentShortDto);
        updatedStudentEntity.setTeacher(teacherEntity);
        return studentMapper.toFullDto(studentRepository.save(updatedStudentEntity));
    }

    public StudentShortDto updateStudentTeacher(Long id, Long teacherId) {
        StudentEntity studentEntity = studentRepository.findById(id)
                .orElseThrow(MissingStudentException::new);

        TeacherEntity teacherEntity = teacherRepository.findByIdAndDeletedFalse(teacherId)
                .orElseThrow(LackOfTeacherException::new);

        if (!teacherEntity.getLanguages().contains(studentEntity.getLanguage())) {
            throw new BadLanguageException();
        }
        studentEntity.setTeacher(teacherEntity);
        studentRepository.save(studentEntity);
        return studentMapper.toShortDto(studentEntity);
    }

    public void deleteStudent(Long id) {
        boolean existsDate = lessonRepository.existsByStudentId(id);
        if (existsDate) {
            throw new LessonInFutureException();
        }
        studentRepository.deleteById(id);
    }
}
