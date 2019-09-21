package de.saxms.language.web.rest;

import de.saxms.language.domain.LanguageEntry;
import de.saxms.language.exception.NotImplementdException;
import de.saxms.language.service.LanguageEntryService;
import de.saxms.language.service.LanguageLocalAdminService;
import de.saxms.language.web.rest.errors.BadRequestAlertException;
import de.saxms.language.web.rest.util.HeaderUtil;
import de.saxms.language.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing LanguageEntry.
 */
@RestController
@RequestMapping("/api")
public class LanguageEntryResource {

    private final Logger log = LoggerFactory.getLogger(LanguageEntryResource.class);

    private static final String ENTITY_NAME = "languageEntry";

    private final LanguageEntryService languageEntryService;
    private final LanguageLocalAdminService languageLocalAdminService;

    public LanguageEntryResource(LanguageEntryService languageEntryService,@Qualifier("FILE") LanguageLocalAdminService languageLocalAdminService) {
        this.languageEntryService = languageEntryService;
        this.languageLocalAdminService = languageLocalAdminService;
    }

    /**
     * POST  /language-entries : Create a new languageEntry.
     *
     * @param languageEntry the languageEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new languageEntry, or with status 400 (Bad Request) if the languageEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/language-entries")
    public ResponseEntity<LanguageEntry> createLanguageEntry(@RequestBody LanguageEntry languageEntry) throws URISyntaxException {
        log.debug("REST request to save LanguageEntry : {}", languageEntry);
        if (languageEntry.getId() != null) {
            throw new BadRequestAlertException("A new languageEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        languageEntry.setLastUpdate(Instant.now());
        LanguageEntry result = languageEntryService.save(languageEntry);
        return ResponseEntity.created(new URI("/api/language-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /language-entries : Updates an existing languageEntry.
     *
     * @param languageEntry the languageEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated languageEntry,
     * or with status 400 (Bad Request) if the languageEntry is not valid,
     * or with status 500 (Internal Server Error) if the languageEntry couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/language-entries")
    public ResponseEntity<LanguageEntry> updateLanguageEntry(@RequestBody LanguageEntry languageEntry) throws URISyntaxException {
        log.debug("REST request to update LanguageEntry : {}", languageEntry);
        if (languageEntry.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LanguageEntry result = languageEntryService.save(languageEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, languageEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /language-entries : get all the languageEntries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of languageEntries in body
     */
    @GetMapping("/language-entries")
    public ResponseEntity<List<LanguageEntry>> getAllLanguageEntries(Pageable pageable) {
        log.debug("REST request to get a page of LanguageEntries");
        Page<LanguageEntry> page = languageEntryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/language-entries");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /language-entries/:id : get the "id" languageEntry.
     *
     * @param id the id of the languageEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the languageEntry, or with status 404 (Not Found)
     */
    @GetMapping("/language-entries/{id}")
    public ResponseEntity<LanguageEntry> getLanguageEntry(@PathVariable Long id) {
        log.debug("REST request to get LanguageEntry : {}", id);
        Optional<LanguageEntry> languageEntry = languageEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(languageEntry);
    }

    /**
     * DELETE  /language-entries/:id : delete the "id" languageEntry.
     *
     * @param id the id of the languageEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/language-entries/{id}")
    public ResponseEntity<Void> deleteLanguageEntry(@PathVariable Long id) {
        log.debug("REST request to delete LanguageEntry : {}", id);
        languageEntryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET   /language-entries/{local}/conflict : get entry id of local for conflict
     *
     * @param local that need conflict resolved
     * @return
     */
    @GetMapping("/language-entries/{local}/conflict")
    public ResponseEntity<List<String>> getConflictEntry(@PathVariable String local) {
        log.debug("REST request to get entry with conflicts in local :{}",local);
        return ResponseEntity.ok().body(languageLocalAdminService.getConflictEntry(local));

    }

    /**
    * GET   /language-entries/untranslated : get id of entries tha non translated in list of locals
    */
    @GetMapping("/language-entries/untranslated")
    public ResponseEntity< Map<String, List<String>>> unTranslatedEntry() {
        log.debug("REST request to get untranslated entry");
        return ResponseEntity.ok().body(languageLocalAdminService.unTranslatedEntry());

    }
}
