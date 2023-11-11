package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.*;

import java.util.ArrayList;

@Component
public class KursantMapper {

    public KursantDto fromEntityToDto(KursantEntity entity) {
        return new KursantDto()
                .setId(entity.getId())
                .setImie(entity.getImie())
                .setNazwisko(entity.getNazwisko())
                .setJezyk(entity.getJezyk())
                .setUsuniety(entity.getUsuniety())
                .setNauczycielId(entity.getNauczycielEntity().getId());
    }

    public KursantEntity fromDtoToEntity(KursantDto model) {
        return new KursantEntity()
                .setId(model.getId())
                .setImie(model.getImie())
                .setNazwisko(model.getNazwisko())
                .setJezyk(model.getJezyk())
                .setUsuniety(model.getUsuniety());
        // MAPUJ NAUCZYCIELA JESZCZE
    }

    public KursantEntity fromDtoAndEntityToEntity(KursantEntity kursantEntity, KursantDto kursantDto) {
        return new KursantEntity()
                .setId(kursantEntity.getId())
                .setImie(kursantDto.getImie())
                .setNazwisko(kursantDto.getNazwisko())
                .setJezyk(kursantDto.getJezyk())
                .setUsuniety(kursantDto.getUsuniety());
        // MAPUJ NAUCZYCIELA JESZCZE
    }

    public KursantNajwInfoDto fromEntityToKursantNajwInfoDto(KursantEntity entity) {
        return new KursantNajwInfoDto()
                .setId(entity.getId())
                .setImie(entity.getImie())
                .setNazwisko(entity.getNazwisko())
                .setJezyk(entity.getJezyk())
                .setNauczycielId(entity.getNauczycielEntity().getId());
    }

    public KursantEntity fromKursantNajwInfoDtoToEntity(KursantNajwInfoDto kursantNajwInfoDto) {
        return new KursantEntity()
                .setId(kursantNajwInfoDto.getId())
                .setImie(kursantNajwInfoDto.getImie())
                .setNazwisko(kursantNajwInfoDto.getNazwisko())
                .setJezyk(kursantNajwInfoDto.getJezyk())
                .setUsuniety(false);
        // MAPUJ NAUCZYCIELA JESZCZE
    }

    public KursantEntity fromNajwInfoAndEntityToEntity(KursantEntity kursantEntity, KursantNajwInfoDto kursantNajwInfoDto) {
        return new KursantEntity()
                .setId(kursantEntity.getId())
                .setImie(kursantNajwInfoDto.getImie())
                .setNazwisko(kursantNajwInfoDto.getNazwisko())
                .setUsuniety(false)
                .setJezyk(kursantNajwInfoDto.getJezyk());
        // MAPUJ NAUCZYCIELA JESZCZE
    }
}
