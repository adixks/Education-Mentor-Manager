package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LessonMapper;
import pl.szlify.codingapi.model.LessonDto;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final LessonMapper lessonMapper;

    public List<LessonDto> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(lessonMapper::fromDtoToEntity)
                .collect(Collectors.toList());
    }

    public LessonDto getLesson(Long id) {
        return lessonRepository.findById(id)
                .map(lessonMapper::fromDtoToEntity)
                .orElseThrow(NoLessonsException::new);
    }

    public LessonDto addLesson(LessonDto lessonDto) {
        if (lessonDto.getDate().isBefore(LocalDateTime.now())) {
            throw new LessonInPastException();
        }

        TeacherEntity teacherEntity = teacherRepository
                .findByIdAndRemovedFalse(lessonDto.getTeacherId()).orElseThrow(LackofTeacherException::new);

        StudentEntity studentEntity = studentRepository
                .findByIdAndRemovedFalse(lessonDto.getStudentId()).orElseThrow(MissingStudentException::new);

        if (!studentEntity.getTeacherEntity().getId().equals(lessonDto.getTeacherId())) {
            throw new NotYourTeacherException();
        }

        Optional<LessonEntity> existingLesson = lessonRepository.findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate());
        if (existingLesson.isPresent()) {
            throw new BusyTermLectionException();
        }

        LessonEntity lessonEntity = lessonMapper.fromEntityToDto(lessonDto);
        lessonEntity.setTeacherEntity(teacherEntity);
        lessonEntity.setStudentEntity(studentEntity);
        lessonRepository.save(lessonEntity);
        return lessonMapper.fromDtoToEntity(lessonEntity);
    }

    public LessonDto updateEntireLesson(Long id, LessonDto lessonDto) {
        if (lessonDto.getDate().isBefore(LocalDateTime.now())) {
            throw new LessonInPastException();
        }

        TeacherEntity teacherEntity = teacherRepository
                .findByIdAndRemovedFalse(lessonDto.getTeacherId()).orElseThrow(LackofTeacherException::new);

        StudentEntity studentEntity = studentRepository
                .findByIdAndRemovedFalse(lessonDto.getStudentId()).orElseThrow(MissingStudentException::new);

        if (!studentEntity.getTeacherEntity().getId().equals(lessonDto.getTeacherId())) {
            throw new NotYourTeacherException();
        }

        Optional<LessonEntity> existingLesson = lessonRepository.findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate());
        if (existingLesson.isPresent()) {
            throw new BusyTermLectionException();
        }

        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setTeacherEntity(teacherEntity)
                .setStudentEntity(studentEntity)
                .setDate(lessonDto.getDate());

        lessonRepository.save(lessonEntity);
        return lessonMapper.fromDtoToEntity(lessonEntity);
    }

    public LessonDto updateLessonDate(Long id, LocalDateTime localDateTime) {
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(NoLessonsException::new);

        if (localDateTime.isBefore(LocalDateTime.now())) {
            throw new LessonInPastException();
        }

        Optional<LessonEntity> existingLesson = lessonRepository.findByTeacherEntityIdAndDate(lessonEntity.getTeacherEntity().getId(), localDateTime);
        if (existingLesson.isPresent()) {
            throw new BusyTermLectionException();
        }

        lessonEntity.setDate(localDateTime);
        lessonRepository.save(lessonEntity);
        return lessonMapper.fromDtoToEntity(lessonEntity);
    }

    public void deleteLesson(Long id) {
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(NoLessonsException::new);

        if (lessonEntity.getDate().isBefore(LocalDateTime.now())) {
            throw new LessonLearnedException();
        }
        lessonRepository.deleteById(id);
    }
}
