package pl.szlify.codingapi.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.szlify.codingapi.mapper.LanguageMapper;
import pl.szlify.codingapi.model.LanguageEntity;
import pl.szlify.codingapi.model.dto.LanguageShortDto;
import pl.szlify.codingapi.repository.LanguageRepository;

@Service
@AllArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public Page<LanguageShortDto> getList(Pageable pageable) {
        return languageRepository.findAll(pageable).map(languageMapper::toShortDto);
    }

    public LanguageShortDto addLanguage(LanguageShortDto languageShortDto) {
        LanguageEntity languageEntity = languageMapper.toEntity(languageShortDto);
        return languageMapper.toShortDto(languageRepository.save(languageEntity));
    }

    public void deleteLanguage(Long id) {
        languageRepository.deleteById(id);
    }
}
