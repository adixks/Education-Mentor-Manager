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
                .map(lessonMapper::fromEntityToDto)
                .collect(Collectors.toList());
    }

    public LessonDto getLesson(Long id) {
        return lessonRepository.findById(id)
                .map(lessonMapper::fromEntityToDto)
                .orElseThrow(NoLessonsException::new);
    }

    public LessonDto addLesson(LessonDto lessonDto) {

        TeacherEntity teacherEntity = teacherRepository
                .findByIdAndRemovedFalse(lessonDto.getTeacherId()).orElseThrow(LackOfTeacherException::new);

        StudentEntity studentEntity = studentRepository
                .findByIdAndRemovedFalse(lessonDto.getStudentId()).orElseThrow(MissingStudentException::new);

        if (!studentEntity.getTeacherEntity().getId().equals(lessonDto.getTeacherId())) {
            throw new NotYourTeacherException();
        }

        Optional<LessonEntity> existingLesson = lessonRepository
                .findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate());
        if (existingLesson.isPresent()) {
            throw new BusyTermLessonException();
        }

        LessonEntity lessonEntity = lessonMapper.fromDtoToEntity(lessonDto);
        lessonEntity.setTeacherEntity(teacherEntity);
        lessonEntity.setStudentEntity(studentEntity);
        lessonRepository.save(lessonEntity);
        return lessonMapper.fromEntityToDto(lessonEntity);
    }

    public LessonDto updateEntireLesson(Long id, LessonDto lessonDto) {

        TeacherEntity teacherEntity = teacherRepository
                .findByIdAndRemovedFalse(lessonDto.getTeacherId()).orElseThrow(LackOfTeacherException::new);

        StudentEntity studentEntity = studentRepository
                .findByIdAndRemovedFalse(lessonDto.getStudentId()).orElseThrow(MissingStudentException::new);

        if (!studentEntity.getTeacherEntity().getId().equals(lessonDto.getTeacherId())) {
            throw new NotYourTeacherException();
        }

        Optional<LessonEntity> existingLesson = lessonRepository
                .findByTeacherEntityIdAndDate(lessonDto.getTeacherId(), lessonDto.getDate());
        if (existingLesson.isPresent()) {
            throw new BusyTermLessonException();
        }

        LessonEntity lessonEntity = new LessonEntity()
                .setId(id)
                .setTeacherEntity(teacherEntity)
                .setStudentEntity(studentEntity)
                .setDate(lessonDto.getDate());

        lessonRepository.save(lessonEntity);
        return lessonMapper.fromEntityToDto(lessonEntity);
    }

    public LessonDto updateLessonDate(Long id, LocalDateTime localDateTime) {
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(NoLessonsException::new);

        Optional<LessonEntity> existingLesson = lessonRepository
                .findByTeacherEntityIdAndDate(lessonEntity.getTeacherEntity().getId(), localDateTime);
        if (existingLesson.isPresent()) {
            throw new BusyTermLessonException();
        }

        lessonEntity.setDate(localDateTime);
        lessonRepository.save(lessonEntity);
        return lessonMapper.fromEntityToDto(lessonEntity);
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
