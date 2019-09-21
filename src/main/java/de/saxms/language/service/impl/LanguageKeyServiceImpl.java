package de.saxms.language.service.impl;

import de.saxms.language.service.LanguageKeyService;
import de.saxms.language.domain.LanguageKey;
import de.saxms.language.repository.LanguageKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing LanguageKey.
 */
@Service
@Transactional
public class LanguageKeyServiceImpl implements LanguageKeyService {

    private final Logger log = LoggerFactory.getLogger(LanguageKeyServiceImpl.class);

    private final LanguageKeyRepository languageKeyRepository;

    public LanguageKeyServiceImpl(LanguageKeyRepository languageKeyRepository) {
        this.languageKeyRepository = languageKeyRepository;
    }

    /**
     * Save a languageKey.
     *
     * @param languageKey the entity to save
     * @return the persisted entity
     */
    @Override
    public LanguageKey save(LanguageKey languageKey) {
        log.debug("Request to save LanguageKey : {}", languageKey);
        return languageKeyRepository.save(languageKey);
    }

    /**
     * Get all the languageKeys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LanguageKey> findAll(Pageable pageable) {
        log.debug("Request to get all LanguageKeys");
        return languageKeyRepository.findAll(pageable);
    }


    /**
     * Get one languageKey by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LanguageKey> findOne(Long id) {
        log.debug("Request to get LanguageKey : {}", id);
        return languageKeyRepository.findById(id);
    }

    /**
     * Delete the languageKey by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LanguageKey : {}", id);
        languageKeyRepository.deleteById(id);
    }
}
