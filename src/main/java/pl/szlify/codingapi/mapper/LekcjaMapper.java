package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.LekcjaEntity;
import pl.szlify.codingapi.model.LekcjaDto;

@Component
public class LekcjaMapper {

    public LekcjaDto fromDtoToEntity(LekcjaEntity entity) {
        return new LekcjaDto()
                .setId(entity.getId())
                .setNauczycielId(entity.getNauczycielEntity().getId())
                .setKursantId(entity.getKursantEntity().getId())
                .setTermin(entity.getTermin());
    }

    public LekcjaEntity fromEntityToDto(LekcjaDto model) {
        return new LekcjaEntity()
                .setId(model.getId())
                .setTermin(model.getTermin());
        // MAPUJ NAUCZYCIELA I KURSANTA
    }
}

