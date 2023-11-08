package pl.szlify.codingapi.mapper;

import org.springframework.stereotype.Component;
import pl.szlify.codingapi.entity.Lekcja;
import pl.szlify.codingapi.model.LekcjaModel;

@Component
public class LekcjaMapper {

    public LekcjaModel from(Lekcja entity) {
        LekcjaModel model = new LekcjaModel();
        model.setId(entity.getId());
        model.setNauczycielId(entity.getNauczyciel().getId());
        model.setKursantId(entity.getKursant().getId());
        model.setTermin(entity.getTermin());
        return model;
    }

    public Lekcja from(LekcjaModel model) {
        Lekcja entity = new Lekcja();
        entity.setId(model.getId());
        entity.setTermin(model.getTermin());
        // MAPUJ NAUCZYCIELA I KURSANTA
        return entity;
    }
}

