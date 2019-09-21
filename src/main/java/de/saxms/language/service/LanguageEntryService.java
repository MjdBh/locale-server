package de.saxms.language.service;

import de.saxms.language.domain.LanguageEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing LanguageEntry.
 */
public interface LanguageEntryService {

    /**
     * Save a languageEntry.
     *
     * @param languageEntry the entity to save
     * @return the persisted entity
     */
    LanguageEntry save(LanguageEntry languageEntry);

    /**
     * Get all the languageEntries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LanguageEntry> findAll(Pageable pageable);


    /**
     * Get the "id" languageEntry.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LanguageEntry> findOne(Long id);

    /**
     * Delete the "id" languageEntry.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
