package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.model.NauczycielEntity;
import pl.szlify.codingapi.exceptions.BrakNauczycielaException;
import pl.szlify.codingapi.mapper.NauczycielMapper;
import pl.szlify.codingapi.model.NauczycieDto;
import pl.szlify.codingapi.model.NauczycielNajwInfoDto;
import pl.szlify.codingapi.repository.NauczycielRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NauczycielService {
    private final NauczycielRepository nauczycielRepository;
    private final NauczycielMapper nauczycielMapper;

    public List<NauczycielNajwInfoDto> pobierzNauczycieli() {
        List<NauczycielEntity> nauczyciele = nauczycielRepository.findAll();
        return nauczyciele.stream()
                .map(nauczycielMapper::fromEntityToNajwInfoDto)
                .collect(Collectors.toList());
    }

    public NauczycieDto pobierzNauczyciela(Long id) {
        NauczycielEntity nauczycielEntity = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        return nauczycielMapper.fromEntityToDto(nauczycielEntity);
    }

    public NauczycieDto dodajNauczyciela(NauczycielNajwInfoDto nauczycielNajwInfoDto) {
        NauczycielEntity nauczycielEntity = nauczycielMapper.fromNauczycielNajwInfoToEntity(nauczycielNajwInfoDto);
        return nauczycielMapper.fromEntityToDto(nauczycielRepository.save(nauczycielEntity));
    }

    public NauczycielNajwInfoDto aktualizujCalegoNauczyciela(Long id, NauczycielNajwInfoDto nauczycielNajwInfoDto) {
        NauczycielEntity nauczycielEntity = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        NauczycielEntity updatedNauczycielEntity = nauczycielMapper.fromNajwInfoAndEntityToEntity(nauczycielEntity, nauczycielNajwInfoDto);
        return nauczycielMapper.fromEntityToNajwInfoDto(nauczycielRepository.save(updatedNauczycielEntity));
    }

    public NauczycielNajwInfoDto aktualizujNauczyciela(Long id, List<String> listaJezykow) {
        NauczycielEntity nauczycielEntity = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        if (listaJezykow != null) {
            List<String> noweJezyki = listaJezykow.stream()
                    .distinct()
                    .filter(jezyk -> !nauczycielEntity.getJezyki().contains(jezyk))
                    .toList();

            nauczycielEntity.getJezyki().addAll(noweJezyki);
        }
        return nauczycielMapper.fromEntityToNajwInfoDto(nauczycielRepository.save(nauczycielEntity));
    }

    public void usunNauczyciela(Long id) {
        NauczycielEntity nauczyciel = nauczycielRepository.findById(id)
                .orElseThrow(BrakNauczycielaException::new);
        nauczyciel.setUsuniety(true);
        nauczycielRepository.save(nauczyciel);
    }
}
