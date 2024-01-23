package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.szlify.codingapi.exceptions.LessonInFutureException;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.exceptions.LackOfTeacherException;
import pl.szlify.codingapi.mapper.TeacherMapper;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;
import pl.szlify.codingapi.repository.LanguageRepository;
import pl.szlify.codingapi.repository.LessonRepository;
import pl.szlify.codingapi.repository.TeacherRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final LessonRepository lessonRepository;
    private final LanguageRepository languageRepository;
    private final TeacherMapper teacherMapper;

    public Page<TeacherShortDto> getList(Pageable pageable) {
        return teacherRepository.findAll(pageable).map(teacherMapper::toShortDto);
    }

    public TeacherFullDto getTeacher(Long id) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);
        return teacherMapper.toFullDto(teacherEntity);
    }

    public TeacherFullDto addTeacher(TeacherShortDto teacherShortDto) {
        TeacherEntity teacherEntity = teacherMapper.toEntity(teacherShortDto);

        Set<String> languageNames = teacherShortDto.getLanguages();
        Set<LanguageEntity> languages = new HashSet<>();

        for (String languageName : languageNames) {
            if (!languageRepository.existsByName(languageName)) {
                LanguageEntity newLanguage = new LanguageEntity();
                newLanguage.setName(languageName);
                languageRepository.save(newLanguage);
                languages.add(newLanguage);
            } else {
                languages.add(languageRepository.findByName(languageName).get());
            }
        }

        teacherEntity.setLanguages(languages);
        return teacherMapper.toFullDto(teacherRepository.save(teacherEntity));
    }

    @Transactional
    public TeacherShortDto updateEntireTeacher(Long id, TeacherShortDto teacherShortDto) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);

        TeacherEntity updatedTeacherEntity = teacherMapper
                .toEntityUpdate(teacherEntity, teacherShortDto);

        Set<String> languageNames = new HashSet<>(teacherShortDto.getLanguages());
        Set<LanguageEntity> languages = new HashSet<>();

        for (String languageName : languageNames) {
            if (!languageRepository.existsByName(languageName)) {
                LanguageEntity newLanguage = new LanguageEntity();
                newLanguage.setName(languageName);
                languageRepository.save(newLanguage);
                languages.add(newLanguage);
            } else {
                languages.add(languageRepository.findByName(languageName).get());
            }
        }

        updatedTeacherEntity.setLanguages(languages);
        return teacherMapper.toShortDto(teacherRepository.save(updatedTeacherEntity));
    }
    public TeacherShortDto updateTeacherLanguagesList(Long id, List<String> languagesList) {
        TeacherEntity teacherEntity = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);

        Set<String> languageNames = new HashSet<>(languagesList);
        Set<LanguageEntity> languages = new HashSet<>();

        for (String languageName : languageNames) {
            if (!languageRepository.existsByName(languageName)) {
                LanguageEntity newLanguage = new LanguageEntity();
                newLanguage.setName(languageName);
                languageRepository.save(newLanguage);
                languages.add(newLanguage);
            } else {
                languages.add(languageRepository.findByName(languageName).get());
            }
        }

        teacherEntity.setLanguages(languages);
        return teacherMapper.toShortDto(teacherRepository.save(teacherEntity));
    }

    public void deleteTeacher(Long id) {
        TeacherEntity teacher = teacherRepository.findById(id)
                .orElseThrow(LackOfTeacherException::new);

        boolean existsDate = lessonRepository.existsByTeacherId(id);
        if (existsDate) {
            throw new LessonInFutureException();
        }
        teacherRepository.delete(teacher);
    }
}
