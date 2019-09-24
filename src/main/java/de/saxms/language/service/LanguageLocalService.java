package de.saxms.language.service;

import de.saxms.language.domain.LanguageLocale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing LanguageLocale.
 */
public interface LanguageLocalService {

    /**
     * Save a languageLocale.
     *
     * @param languageLocale the entity to save
     * @return the persisted entity
     */
    LanguageLocale save(LanguageLocale languageLocale);

    /**
     * Get all the languageLocals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LanguageLocale> findAll(Pageable pageable);


    /**
     * Get the "id" languageLocal.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LanguageLocale> findOne(Long id);

    /**
     * Delete the "id" languageLocal.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
