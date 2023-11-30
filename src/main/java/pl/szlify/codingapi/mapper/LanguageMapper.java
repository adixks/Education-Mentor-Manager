package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.dto.LanguageShortDto;

import java.util.HashSet;

@Component
public class LanguageMapper {

    public LanguageShortDto toShortDto(LanguageEntity entity) {
        return new LanguageShortDto()
                .setId(entity.getId())
                .setName(entity.getName());
    }

    public LanguageEntity toEntity(LanguageShortDto model) {
        return new LanguageEntity()
                .setId(model.getId())
                .setName(model.getName())
                .setStudents(new HashSet<>())
                .setTeachers(new HashSet<>());
    }
}
