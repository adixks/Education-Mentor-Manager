package pl.szlify.codingapi.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.model.KursantEntity;
import pl.szlify.codingapi.model.NauczycielEntity;
import pl.szlify.codingapi.model.NauczycieDto;
import pl.szlify.codingapi.model.NauczycielNajwInfoDto;

@Component
public class NauczycielMapper {

    public NauczycieDto fromEntityToDto(NauczycielEntity entity) {
        NauczycieDto model = new NauczycieDto();
        model.setId(entity.getId());
        model.setImie(entity.getImie());
        model.setNazwisko(entity.getNazwisko());
        model.setUsuniety(entity.getUsuniety());
        model.setJezyki(entity.getJezyki());

        if (entity.getListaKursantow() == null) {
            model.setListaKursantow(new ArrayList<>());
        } else {
            model.setListaKursantow(entity.getListaKursantow().stream().map(KursantEntity::getId).collect(Collectors.toList()));
        }
        return model;
    }

    public NauczycielEntity fromDtoToEntity(NauczycieDto model) {
        return new NauczycielEntity()
                .setId(model.getId())
                .setImie(model.getImie())
                .setNazwisko(model.getNazwisko())
                .setUsuniety(model.getUsuniety())
                .setJezyki(model.getJezyki());
        // MAPUJ LISTE KURSANTOW
    }

    public NauczycielNajwInfoDto fromEntityToNajwInfoDto(NauczycielEntity entity) {
        return new NauczycielNajwInfoDto()
                .setId(entity.getId())
                .setImie(entity.getImie())
                .setNazwisko(entity.getNazwisko())
                .setJezyki(entity.getJezyki());
    }

    public NauczycielEntity fromNauczycielNajwInfoToEntity(NauczycielNajwInfoDto nauczycielNajwInfoDto) {
        return new NauczycielEntity()
                .setId(nauczycielNajwInfoDto.getId())
                .setImie(nauczycielNajwInfoDto.getImie())
                .setNazwisko(nauczycielNajwInfoDto.getNazwisko())
                .setUsuniety(false)
                .setListaKursantow(new ArrayList<>())
                .setJezyki(nauczycielNajwInfoDto.getJezyki());
    }

    public NauczycielEntity fromNajwInfoAndEntityToEntity(NauczycielEntity nauczycielEntity, NauczycielNajwInfoDto nauczycielNajwInfoDto) {
        return new NauczycielEntity()
                .setId(nauczycielEntity.getId())
                .setImie(nauczycielNajwInfoDto.getImie())
                .setNazwisko(nauczycielNajwInfoDto.getNazwisko())
                .setUsuniety(false)
                .setJezyki(nauczycielNajwInfoDto.getJezyki())
                .setListaKursantow(new ArrayList<>());
    }
}
