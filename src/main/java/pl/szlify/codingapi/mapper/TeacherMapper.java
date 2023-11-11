package pl.szlify.codingapi.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.StudentEntity;
import pl.szlify.codingapi.model.TeacherEntity;
import pl.szlify.codingapi.model.TeacherDto;
import pl.szlify.codingapi.model.TeacherBasicInfoDto;

@Component
public class TeacherMapper {

    public TeacherDto fromEntityToDto(TeacherEntity entity) {
        TeacherDto model = new TeacherDto();
        model.setId(entity.getId());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setRemoved(entity.getRemoved());
        model.setLanguages(entity.getLanguages());

        if (entity.getStudentsList() == null) {
            model.setStudentsList(new ArrayList<>());
        } else {
            model.setStudentsList(entity.getStudentsList().stream().map(StudentEntity::getId).collect(Collectors.toList()));
        }
        return model;
    }

    public TeacherEntity fromDtoToEntity(TeacherDto model) {
        return new TeacherEntity()
                .setId(model.getId())
                .setFirstName(model.getFirstName())
                .setLastName(model.getLastName())
                .setRemoved(model.getRemoved())
                .setLanguages(model.getLanguages());
        // MAP COURSE LIST
    }

    public TeacherBasicInfoDto fromEntityToNajwInfoDto(TeacherEntity entity) {
        return new TeacherBasicInfoDto()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setLanguages(entity.getLanguages());
    }

    public TeacherEntity fromNajwInfoToEntity(TeacherBasicInfoDto teacherBasicInfoDto) {
        return new TeacherEntity()
                .setId(teacherBasicInfoDto.getId())
                .setFirstName(teacherBasicInfoDto.getFirstName())
                .setLastName(teacherBasicInfoDto.getLastName())
                .setRemoved(false)
                .setStudentsList(new ArrayList<>())
                .setLanguages(teacherBasicInfoDto.getLanguages());
    }

    public TeacherEntity fromNajwInfoAndEntityToEntity(TeacherEntity teacherEntity, TeacherBasicInfoDto teacherBasicInfoDto) {
        return new TeacherEntity()
                .setId(teacherEntity.getId())
                .setFirstName(teacherBasicInfoDto.getFirstName())
                .setLastName(teacherBasicInfoDto.getLastName())
                .setRemoved(false)
                .setLanguages(teacherBasicInfoDto.getLanguages())
                .setStudentsList(new ArrayList<>());
    }
}
