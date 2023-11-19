package pl.szlify.codingapi.mapper;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.model.dto.TeacherFullDto;
import pl.szlify.codingapi.model.dto.TeacherShortDto;

@Component
public class TeacherMapper {

    public TeacherFullDto toFullDto(TeacherEntity entity) {
        TeacherFullDto model = new TeacherFullDto()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setRemoved(entity.getDeleted())
                .setLanguages(entity.getLanguages());

        if (entity.getStudentsList() != null) {
            model.setStudentsListIds(entity.getStudentsList().stream().map(StudentEntity::getId).collect(Collectors.toSet()));
        }

        if (entity.getLessons() != null) {
            model.setLessonIds(entity.getLessons().stream().map(LessonEntity::getId).collect(Collectors.toSet()));
        }

        return model;
    }

    public TeacherShortDto toShortDto(TeacherEntity entity) {
        return new TeacherShortDto()
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setLanguages(entity.getLanguages());
    }

    public TeacherEntity toEntity(TeacherShortDto teacherShortDto) {
        return new TeacherEntity()
                .setFirstName(teacherShortDto.getFirstName())
                .setLastName(teacherShortDto.getLastName())
                .setDeleted(false)
                .setStudentsList(new HashSet<>())
                .setLanguages(teacherShortDto.getLanguages())
                .setLessons(new HashSet<>());
    }

    public TeacherEntity toEntityUpdate(TeacherEntity teacherEntity, TeacherShortDto teacherShortDto) {
        teacherEntity.setFirstName(teacherShortDto.getFirstName());
        teacherEntity.setLastName(teacherShortDto.getLastName());
        teacherEntity.setLanguages(teacherShortDto.getLanguages());
        return teacherEntity;
    }
}
