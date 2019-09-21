package de.saxms.language.service.impl;

import de.saxms.language.service.LanguageEntryService;
import de.saxms.language.domain.LanguageEntry;
import de.saxms.language.repository.LanguageEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing LanguageEntry.
 */
@Service
@Transactional
public class LanguageEntryServiceImpl implements LanguageEntryService {

    private final Logger log = LoggerFactory.getLogger(LanguageEntryServiceImpl.class);

    private final LanguageEntryRepository languageEntryRepository;

    public LanguageEntryServiceImpl(LanguageEntryRepository languageEntryRepository) {
        this.languageEntryRepository = languageEntryRepository;
    }

    /**
     * Save a languageEntry.
     *
     * @param languageEntry the entity to save
     * @return the persisted entity
     */
    @Override
    public LanguageEntry save(LanguageEntry languageEntry) {
        log.debug("Request to save LanguageEntry : {}", languageEntry);
        return languageEntryRepository.save(languageEntry);
    }

    /**
     * Get all the languageEntries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LanguageEntry> findAll(Pageable pageable) {
        log.debug("Request to get all LanguageEntries");
        return languageEntryRepository.findAll(pageable);
    }


    /**
     * Get one languageEntry by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LanguageEntry> findOne(Long id) {
        log.debug("Request to get LanguageEntry : {}", id);
        return languageEntryRepository.findById(id);
    }

    /**
     * Delete the languageEntry by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LanguageEntry : {}", id);
        languageEntryRepository.deleteById(id);
    }
}
