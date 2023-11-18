package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.LessonEntity;
import pl.szlify.codingapi.model.LessonDto;

@Component
public class LessonMapper {

    public LessonDto fromEntityToDto(LessonEntity entity) {
        return new LessonDto()
                .setId(entity.getId())
                .setTeacherId(entity.getTeacherEntity().getId())
                .setStudentId(entity.getStudentEntity().getId())
                .setDate(entity.getDate());
    }

    public LessonEntity fromDtoToEntity(LessonDto model) {
        return new LessonEntity()
                .setId(model.getId())
                .setDate(model.getDate());
        // MAP THE TEACHER AND THE STUDENT
    }
}
