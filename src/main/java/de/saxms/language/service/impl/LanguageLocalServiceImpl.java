package de.saxms.language.service.impl;

import de.saxms.language.domain.LanguageLocale;
import de.saxms.language.service.LanguageLocalService;
import de.saxms.language.repository.LanguageLocalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing LanguageLocale.
 */
@Service
@Transactional
public class LanguageLocalServiceImpl implements LanguageLocalService {

    private final Logger log = LoggerFactory.getLogger(LanguageLocalServiceImpl.class);

    private final LanguageLocalRepository languageLocalRepository;

    public LanguageLocalServiceImpl(LanguageLocalRepository languageLocalRepository) {
        this.languageLocalRepository = languageLocalRepository;
    }

    /**
     * Save a languageLocale.
     *
     * @param languageLocale the entity to save
     * @return the persisted entity
     */
    @Override
    public LanguageLocale save(LanguageLocale languageLocale) {
        log.debug("Request to save LanguageLocale : {}", languageLocale);
        return languageLocalRepository.save(languageLocale);
    }

    /**
     * Get all the languageLocals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LanguageLocale> findAll(Pageable pageable) {
        log.debug("Request to get all LanguageLocals");
        return languageLocalRepository.findAll(pageable);
    }


    /**
     * Get one languageLocal by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LanguageLocale> findOne(Long id) {
        log.debug("Request to get LanguageLocale : {}", id);
        return languageLocalRepository.findById(id);
    }

    /**
     * Delete the languageLocal by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete LanguageLocale : {}", id);
        languageLocalRepository.deleteById(id);
    }
}
