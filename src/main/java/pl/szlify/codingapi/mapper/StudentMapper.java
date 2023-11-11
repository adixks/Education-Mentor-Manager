package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.*;

@Component
public class StudentMapper {

    public StudentDto fromEntityToDto(StudentEntity entity) {
        return new StudentDto()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setLanguage(entity.getLanguage())
                .setRemoved(entity.getRemoved())
                .setTeacherId(entity.getTeacherEntity().getId());
    }

    public StudentEntity fromDtoToEntity(StudentDto model) {
        return new StudentEntity()
                .setId(model.getId())
                .setFirstName(model.getFirstName())
                .setLastName(model.getLastName())
                .setLanguage(model.getLanguage())
                .setRemoved(model.getRemoved());
        // MAP THE TEACHER
    }

    public StudentEntity fromDtoAndEntityToEntity(StudentEntity studentEntity, StudentDto studentDto) {
        return new StudentEntity()
                .setId(studentEntity.getId())
                .setFirstName(studentDto.getFirstName())
                .setLastName(studentDto.getLastName())
                .setLanguage(studentDto.getLanguage())
                .setRemoved(studentDto.getRemoved());
        // MAP THE TEACHER
    }

    public StudentBasicInfoDto fromEntityToBasicInfoDto(StudentEntity entity) {
        return new StudentBasicInfoDto()
                .setId(entity.getId())
                .setFirstName(entity.getFirstName())
                .setLastName(entity.getLastName())
                .setLanguage(entity.getLanguage())
                .setTeacherId(entity.getTeacherEntity().getId());
    }

    public StudentEntity fromBasicInfoDtoToEntity(StudentBasicInfoDto studentBasicInfoDto) {
        return new StudentEntity()
                .setId(studentBasicInfoDto.getId())
                .setFirstName(studentBasicInfoDto.getFirstName())
                .setLastName(studentBasicInfoDto.getLastName())
                .setLanguage(studentBasicInfoDto.getLanguage())
                .setRemoved(false);
        // MAP THE TEACHER
    }

    public StudentEntity fromBasicInfoAndEntityToEntity(StudentEntity studentEntity, StudentBasicInfoDto studentBasicInfoDto) {
        return new StudentEntity()
                .setId(studentEntity.getId())
                .setFirstName(studentBasicInfoDto.getFirstName())
                .setLastName(studentBasicInfoDto.getLastName())
                .setRemoved(false)
                .setLanguage(studentBasicInfoDto.getLanguage());
        // MAP THE TEACHER
    }
}
