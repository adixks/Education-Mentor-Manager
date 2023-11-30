package pl.szlify.codingapi.mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;

@Component
public class TeacherMapper {

    public TeacherFullDto toFullDto(TeacherEntity entity) {
        TeacherFullDto model = new TeacherFullDto()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setRemoved(entity.getDeleted());

        if (entity.getStudentsList() != null) {
            model.setStudentsListIds(entity.getStudentsList().stream()
                    .map(StudentEntity::getId)
                    .collect(Collectors.toSet()));
        }

        if (entity.getLessons() != null) {
            model.setLessonIds(entity.getLessons().stream()
                    .map(LessonEntity::getId)
                    .collect(Collectors.toSet()));
        }

        if (entity.getLanguages() != null) {
            model.setLanguages(entity.getLanguages().stream()
                    .map(LanguageEntity::getName)
                    .collect(Collectors.toSet()));
        }
        return model;
    }

    public TeacherShortDto toShortDto(TeacherEntity entity) {
        TeacherShortDto model = new TeacherShortDto()
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName());

        if (entity.getLanguages() != null) {
            model.setLanguages(entity.getLanguages().stream()
                    .map(LanguageEntity::getName)
                    .collect(Collectors.toSet()));
        }
        return model;
    }

    public TeacherEntity toEntity(TeacherShortDto teacherShortDto) {
        return new TeacherEntity()
                .setFirstName(teacherShortDto.getFirstName())
                .setLastName(teacherShortDto.getLastName())
                .setDeleted(false)
                .setStudentsList(new HashSet<>())
                .setLessons(new HashSet<>());
        // MAP LANGUAGE
    }

    public TeacherEntity toEntityUpdate(TeacherEntity teacherEntity, TeacherShortDto teacherShortDto) {
        return new TeacherEntity()
                .setId(teacherEntity.getId())
                .setDeleted(false)
                .setFirstName(teacherShortDto.getFirstName())
                .setLastName(teacherShortDto.getLastName());
        //MAP LANGUAGE
    }
}
