package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.entity.Kursant;
import pl.szlify.codingapi.model.KursantModel;

@Component
public class KursantMapper {

    public KursantModel from(Kursant entity) {
        KursantModel model = new KursantModel();
        model.setId(entity.getId());
        model.setImie(entity.getImie());
        model.setNazwisko(entity.getNazwisko());
        model.setJezyk(entity.getJezyk());
        model.setUsuniety(entity.getUsuniety());
        model.setNauczycielId(entity.getNauczyciel().getId());
        return model;
    }

    public Kursant from(KursantModel model) {
        Kursant entity = new Kursant();
        entity.setId(model.getId());
        entity.setImie(model.getImie());
        entity.setNazwisko(model.getNazwisko());
        entity.setJezyk(model.getJezyk());
        entity.setUsuniety(model.getUsuniety());
        // MAPUJ NAUCZYCIELA JESZCZE
        return entity;
    }
}
