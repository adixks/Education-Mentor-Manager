package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final TeacherMapper teacherMapper;

    public List<TeacherShortDto> getTeachersList() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public TeacherFullDto getTeacher(Long id) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);
        return teacherMapper.toFullDto(teacherEntity);
    }

    public TeacherFullDto addTeacher(TeacherShortDto teacherShortDto) {
        TeacherEntity teacherEntity = teacherMapper.toEntity(teacherShortDto);
        return teacherMapper.toFullDto(teacherRepository.save(teacherEntity));
    }

    @Transactional
    public TeacherShortDto updateEntireTeacher(Long id, TeacherShortDto teacherShortDto) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);
        TeacherEntity updatedTeacherEntity = teacherMapper
                .toEntityUpdate(teacherEntity, teacherShortDto);
        return teacherMapper.toShortDto(teacherRepository.save(updatedTeacherEntity));
    }

    public TeacherShortDto updateTeacherLanguagesList(Long id, List<String> languagesList) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);
        List<String> newLanguages = languagesList.stream()
                .distinct()
                .filter(language -> !teacherEntity.getLanguages().contains(language))
                .toList();
        List<String> updatedLanguages = new ArrayList<>(teacherEntity.getLanguages());
        updatedLanguages.addAll(newLanguages);
        teacherEntity.setLanguages(updatedLanguages);
        return teacherMapper.toShortDto(teacherRepository.save(teacherEntity));
    }

    public void deleteTeacher(Long id) {
        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);
        boolean existsDate = lessonRepository.existsByTeacherId(id);
        if (existsDate) {
            throw new LessonInFutureException();
        }
        teacher.setDeleted(true);
        teacherRepository.save(teacher);
    }
}
