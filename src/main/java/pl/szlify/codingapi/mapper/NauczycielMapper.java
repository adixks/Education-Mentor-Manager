package pl.szlify.codingapi.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import pl.szlify.codingapi.entity.Kursant;
import pl.szlify.codingapi.entity.Nauczyciel;
import pl.szlify.codingapi.model.NauczycielModel;

@Component
public class NauczycielMapper {

    public NauczycielModel from(Nauczyciel entity) {
        NauczycielModel model = new NauczycielModel();
        model.setId(entity.getId());
        model.setImie(entity.getImie());
        model.setNazwisko(entity.getNazwisko());
        model.setUsuniety(entity.getUsuniety());
        model.setJezyki(entity.getJezyki());

        if(entity.getListaKursantow() == null){
            model.setListaKursantow(new ArrayList<>());
        } else {
            model.setListaKursantow(entity.getListaKursantow().stream().map(Kursant::getId).collect(Collectors.toList()));
        }
        return model;
    }

    public Nauczyciel from(NauczycielModel model) {
        Nauczyciel entity = new Nauczyciel();
        entity.setId(model.getId());
        entity.setImie(model.getImie());
        entity.setNazwisko(model.getNazwisko());
        entity.setUsuniety(model.getUsuniety());
        entity.setJezyki(model.getJezyki());
        // MAPUJ LISTE KURSANTOW
        return entity;
    }
}
