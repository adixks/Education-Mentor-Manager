package pl.szlify.codingapi.service;

import org.springframework.stereotype.Service;
import pl.szlify.codingapi.entity.Nauczyciel;
import pl.szlify.codingapi.exceptions.BrakNauczycielaException;
import pl.szlify.codingapi.mapper.NauczycielMapper;
import pl.szlify.codingapi.model.NauczycielModel;
import pl.szlify.codingapi.repository.NauczycielRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NauczycielService {
    private NauczycielRepository nauczycielRepository;
    private NauczycielMapper nauczycielMapper;

    public NauczycielService(NauczycielRepository nauczycielRepository, NauczycielMapper nauczycielMapper) {
        this.nauczycielRepository = nauczycielRepository;
        this.nauczycielMapper = nauczycielMapper;
    }

    public List<NauczycielModel> pobierzNauczycieli() {
        List<Nauczyciel> nauczyciele = nauczycielRepository.findAll();
        return nauczyciele.stream()
                .map(nauczycielMapper::from)
                .collect(Collectors.toList());
    }

    public NauczycielModel pobierzNauczyciela(Long id) {
        Nauczyciel nauczyciel = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        return nauczycielMapper.from(nauczyciel);
    }

    public NauczycielModel dodajNauczyciela(NauczycielModel nauczycielModel) {
        Nauczyciel nauczyciel = nauczycielMapper.from(nauczycielModel);
        nauczyciel.setUsuniety(false);
        return nauczycielMapper.from(nauczycielRepository.save(nauczyciel));
    }

    public NauczycielModel aktualizujNauczyciela(Long id, List<String> listaJezykow) {
        Nauczyciel nauczyciel = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        if (listaJezykow != null) {
            List<String> noweJezyki = listaJezykow.stream()
                    .distinct()
                    .filter(jezyk -> !nauczyciel.getJezyki().contains(jezyk))
                    .toList();

            nauczyciel.getJezyki().addAll(noweJezyki);
        }
        return nauczycielMapper.from(nauczycielRepository.save(nauczyciel));
    }

    public void usunNauczyciela(Long id) {
        Nauczyciel nauczyciel = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        nauczyciel.setUsuniety(true);
        nauczycielRepository.save(nauczyciel);
    }
}
