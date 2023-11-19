package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.*;
import pl.szlify.codingapi.model.dto.StudentShortDto;
import pl.szlify.codingapi.model.dto.StudentFullDto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StudentMapper {

    public StudentFullDto toFullDto(StudentEntity entity) {
        Set<Long> lessonIds = new HashSet<>();

        if (entity.getLessons() != null) {
            lessonIds = entity.getLessons().stream()
                    .map(LessonEntity::getId)
                    .collect(Collectors.toSet());
        }

        return new StudentFullDto()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setLanguage(entity.getLanguage())
                .setRemoved(entity.getDeleted())
                .setTeacherId(entity.getTeacher().getId())
                .setLessonIds(lessonIds);
    }

    public StudentShortDto toShortDto(StudentEntity entity) {
        return new StudentShortDto()
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setLanguage(entity.getLanguage())
                .setTeacherId(entity.getTeacher().getId());
    }

    public StudentEntity toEntity(StudentShortDto studentShortDto) {
        return new StudentEntity()
                .setFirstName(studentShortDto.getFirstName())
                .setLastName(studentShortDto.getLastName())
                .setLanguage(studentShortDto.getLanguage())
                .setDeleted(false);
        // MAP THE TEACHER
    }

    public StudentEntity toEntityUpdate(StudentEntity studentEntity, StudentShortDto studentShortDto) {
        return new StudentEntity()
                .setId(studentEntity.getId())
                .setFirstName(studentShortDto.getFirstName())
                .setLastName(studentShortDto.getLastName())
                .setDeleted(false)
                .setLanguage(studentShortDto.getLanguage());
        // MAP THE TEACHER
    }
}
