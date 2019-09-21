package de.saxms.language.service;

import de.saxms.language.domain.LanguageLocal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing LanguageLocal.
 */
public interface LanguageLocalService {

    /**
     * Save a languageLocal.
     *
     * @param languageLocal the entity to save
     * @return the persisted entity
     */
    LanguageLocal save(LanguageLocal languageLocal);

    /**
     * Get all the languageLocals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<LanguageLocal> findAll(Pageable pageable);


    /**
     * Get the "id" languageLocal.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<LanguageLocal> findOne(Long id);

    /**
     * Delete the "id" languageLocal.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
