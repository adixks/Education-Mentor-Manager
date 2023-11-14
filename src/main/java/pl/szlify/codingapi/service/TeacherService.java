package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.exceptions.LackofTeacherException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final TeacherMapper teacherMapper;

    public List<TeacherBasicInfoDto> getTeachersList() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::fromEntityToNajwInfoDto)
                .collect(Collectors.toList());
    }

    public TeacherDto getTeacher(Long id) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackofTeacherException::new);
        return teacherMapper.fromEntityToDto(teacherEntity);
    }

    public TeacherDto addTeacher(TeacherBasicInfoDto teacherBasicInfoDto) {
        TeacherEntity teacherEntity = teacherMapper.fromNajwInfoToEntity(teacherBasicInfoDto);
        return teacherMapper.fromEntityToDto(teacherRepository.save(teacherEntity));
    }

    public TeacherBasicInfoDto updateEntireTeacher(Long id, TeacherBasicInfoDto teacherBasicInfoDto) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackofTeacherException::new);
        TeacherEntity updatedTeacherEntity = teacherMapper
                .fromNajwInfoAndEntityToEntity(teacherEntity, teacherBasicInfoDto);
        return teacherMapper.fromEntityToNajwInfoDto(teacherRepository.save(updatedTeacherEntity));
    }

    public TeacherBasicInfoDto updateTeacherLanguagesList(Long id, List<String> languagesList) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackofTeacherException::new);
        List<String> newLanguages = languagesList.stream()
                .distinct()
                .filter(language -> !teacherEntity.getLanguages().contains(language))
                .toList();
        teacherEntity.getLanguages().addAll(newLanguages);
        return teacherMapper.fromEntityToNajwInfoDto(teacherRepository.save(teacherEntity));
    }

    public void deleteTeacher(Long id) {
        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(LackofTeacherException::new);
        Optional<LessonEntity> existingLesson = lessonRepository.findByTeacherEntityId(id);
        if (existingLesson.isPresent()) {
            throw new LessonInFutureException();
        }
        teacher.setRemoved(true);
        teacherRepository.save(teacher);
    }
}
