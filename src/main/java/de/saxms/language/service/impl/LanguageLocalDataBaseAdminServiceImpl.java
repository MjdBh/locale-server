package de.saxms.language.service.impl;

import de.saxms.language.domain.LanguageEntry;
import de.saxms.language.domain.LanguageKey;
import de.saxms.language.exception.ExistLanguageException;
import de.saxms.language.exception.NotImplementdException;
import de.saxms.language.repository.LanguageEntryRepository;
import de.saxms.language.repository.LanguageKeyRepository;
import de.saxms.language.repository.LanguageLocalRepository;
import de.saxms.language.service.LanguageLocalAdminService;
import de.saxms.language.service.dto.LanguageEntryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Qualifier("DB")
public class LanguageLocalDataBaseAdminServiceImpl implements LanguageLocalAdminService {

    @Autowired
    private LanguageEntryRepository languageEntryRepository;
    @Autowired
    private LanguageKeyRepository languageKeyRepository;
    @Autowired
    private LanguageLocalRepository languageLocalRepository;

    @Override
    public boolean saveEntry(String local, LanguageEntryDTO entryDTO) {
        LanguageEntry languageEntry = new LanguageEntry();
        languageEntry.setEntryValue(entryDTO.getContent());
        languageEntry.setLanguageLocal(languageLocalRepository.getByAbbreviation(local));
        LanguageKey key = languageKeyRepository.getByEntryKey(entryDTO.getContent()).orElse(languageKeyRepository.save(new LanguageKey(entryDTO.getId())));
        languageEntry.setLanguageKey(key);
        languageEntry.setLastUpdate(Instant.now());
        languageEntryRepository.save(languageEntry);
        return true;
    }

    @Override
    public boolean deleteEntry(String local, String entryKey) {
        languageEntryRepository.deleteEntry(local, entryKey);
        return true;
    }

    @Override
    public boolean updateEntry(String local, String entryKey, String entryValue) {
        throw new NotImplementdException();
    }

    @Override
    public void addLanguage(String name, String abbreviation) throws ExistLanguageException {
        throw new NotImplementdException();
    }

    @Override
    public List<String> getAvailableLanguages() {
        throw new NotImplementdException();
    }

    @Override
    public void writeLanguagesToJsonFile() {
        throw new NotImplementdException();
    }

    @Override
    public void removeDuplicates(String local) {
        languageEntryRepository.getDuplicateKey(local).stream().parallel().forEach(e -> languageEntryRepository.deleteEntry(local, e));
    }

    @Override
    public List<String> getConflictEntry(String local) {

        throw new NotImplementdException();
    }

    @Override
    public Map<String, List<String>> unTranslatedEntry() {

        throw new NotImplementdException();
    }
}
