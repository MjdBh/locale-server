package de.saxms.language.service;

import de.saxms.language.domain.LanguageKey;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing LanguageKey.
 */
public interface LanguageKeyService {

    /**
     * Save a languageKey.
     *
     * @param languageKey the entity to save
     * @return the persisted entity
     */
    LanguageKey save(LanguageKey languageKey);

    /**
     * Get all the languageKeys.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LanguageKey> findAll(Pageable pageable);


    /**
     * Get the "id" languageKey.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LanguageKey> findOne(Long id);

    /**
     * Delete the "id" languageKey.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
