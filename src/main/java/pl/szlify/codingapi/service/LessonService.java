package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.exceptions.*;
import pl.szlify.codingapi.mapper.LessonMapper;
import pl.szlify.codingapi.model.dto.LessonDto;
import pl.szlify.codingapi.repository.StudentRepository;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final LessonMapper lessonMapper;

    public Page<LessonDto> getAllLessons(Pageable pageable) {
        return lessonRepository.findAll(pageable).map(lessonMapper::toDto);
    }

    public LessonDto getLesson(Long id) {
        return lessonRepository.findById(id)
                .map(lessonMapper::toDto)
                .orElseThrow(NoLessonsException::new);
    }

    public LessonDto addLesson(LessonDto lessonDto) {
        TeacherEntity teacherEntity = teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())
                .orElseThrow(LackOfTeacherException::new);
        StudentEntity studentEntity = studentRepository.findByIdAndDeletedFalse(lessonDto.getStudentId())
                .orElseThrow(MissingStudentException::new);
        if (!studentEntity.getTeacher().getId().equals(lessonDto.getTeacherId())) {
            throw new NotYourTeacherException();
        }

        LocalDateTime start = lessonDto.getDate();
        LocalDateTime end = start.plusMinutes(60).plusMinutes(15);
        boolean existsDate = lessonRepository
                .existsByTeacherIdAndDateBetween(lessonDto.getTeacherId(), start.minusMinutes(75), end.plusMinutes(75));
        if (existsDate) {
            throw new BusyTermLessonException();
        }

        LessonEntity lessonEntity = lessonMapper.toEntity(lessonDto);
        lessonEntity.setTeacher(teacherEntity);
        lessonEntity.setStudent(studentEntity);
        return lessonMapper.toDto(lessonRepository.save(lessonEntity));
    }

    @Transactional
    public LessonDto updateEntireLesson(Long id, LessonDto lessonDto) {
        TeacherEntity teacherEntity = teacherRepository.findByIdAndDeletedFalse(lessonDto.getTeacherId())
                .orElseThrow(LackOfTeacherException::new);
        StudentEntity studentEntity = studentRepository.findByIdAndDeletedFalse(lessonDto.getStudentId())
                .orElseThrow(MissingStudentException::new);
        if (!studentEntity.getTeacher().getId().equals(lessonDto.getTeacherId())) {
            throw new NotYourTeacherException();
        }
        LocalDateTime start = lessonDto.getDate();
        LocalDateTime end = start.plusMinutes(60).plusMinutes(15);
        List<LessonEntity> lessonsInTimeRange = lessonRepository
                .findByTeacherIdAndDateBetween(lessonDto.getTeacherId(), start.minusMinutes(75), end.plusMinutes(75));
        lessonsInTimeRange.removeIf(lesson -> lesson.getId().equals(id));
        if (!lessonsInTimeRange.isEmpty()) {
            throw new BusyTermLessonException();
        }
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(NoLessonsException::new)
                .setTeacher(teacherEntity)
                .setStudent(studentEntity)
                .setDate(lessonDto.getDate());
        return lessonMapper.toDto(lessonRepository.save(lessonEntity));
    }

    public LessonDto updateLessonDate(Long id, LocalDateTime localDateTime) {
        LessonEntity lessonEntity = lessonRepository.findById(id)
                .orElseThrow(NoLessonsException::new);
        LocalDateTime start = localDateTime;
        LocalDateTime end = start.plusMinutes(60).plusMinutes(15);
        List<LessonEntity> lessonsInTimeRange = lessonRepository
                .findByTeacherIdAndDateBetween(lessonEntity.getTeacher().getId(),
                        start.minusMinutes(75), end.plusMinutes(75));
        lessonsInTimeRange.removeIf(lesson -> lesson.getId().equals(id));
        if (!lessonsInTimeRange.isEmpty()) {
            throw new BusyTermLessonException();
        }
        lessonEntity.setDate(localDateTime);
        return lessonMapper.toDto(lessonRepository.save(lessonEntity));
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
