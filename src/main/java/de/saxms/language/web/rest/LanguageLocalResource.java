package de.saxms.language.web.rest;
import de.saxms.language.domain.LanguageLocale;
import de.saxms.language.service.LanguageLocalService;
import de.saxms.language.web.rest.errors.BadRequestAlertException;
import de.saxms.language.web.rest.util.HeaderUtil;
import de.saxms.language.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LanguageLocale.
 */
@RestController
@RequestMapping("/api")
public class LanguageLocalResource {

    private final Logger log = LoggerFactory.getLogger(LanguageLocalResource.class);

    private static final String ENTITY_NAME = "languageLocal";

    private final LanguageLocalService languageLocalService;

    public LanguageLocalResource(LanguageLocalService languageLocalService) {
        this.languageLocalService = languageLocalService;
    }

    /**
     * POST  /language-locals : Create a new languageLocale.
     *
     * @param languageLocale the languageLocale to create
     * @return the ResponseEntity with status 201 (Created) and with body the new languageLocale, or with status 400 (Bad Request) if the languageLocale has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/language-locals")
    public ResponseEntity<LanguageLocale> createLanguageLocal(@RequestBody LanguageLocale languageLocale) throws URISyntaxException {
        log.debug("REST request to save LanguageLocale : {}", languageLocale);
        if (languageLocale.getId() != null) {
            throw new BadRequestAlertException("A new languageLocale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LanguageLocale result = languageLocalService.save(languageLocale);
        return ResponseEntity.created(new URI("/api/language-locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /language-locals : Updates an existing languageLocale.
     *
     * @param languageLocale the languageLocale to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated languageLocale,
     * or with status 400 (Bad Request) if the languageLocale is not valid,
     * or with status 500 (Internal Server Error) if the languageLocale couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/language-locals")
    public ResponseEntity<LanguageLocale> updateLanguageLocal(@RequestBody LanguageLocale languageLocale) throws URISyntaxException {
        log.debug("REST request to update LanguageLocale : {}", languageLocale);
        if (languageLocale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LanguageLocale result = languageLocalService.save(languageLocale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, languageLocale.getId().toString()))
            .body(result);
    }

    /**
     * GET  /language-locals : get all the languageLocals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of languageLocals in body
     */
    @GetMapping("/language-locals")
    public ResponseEntity<List<LanguageLocale>> getAllLanguageLocals(Pageable pageable) {
        log.debug("REST request to get a page of LanguageLocals");
        Page<LanguageLocale> page = languageLocalService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/language-locals");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /language-locals/:id : get the "id" languageLocal.
     *
     * @param id the id of the languageLocal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the languageLocal, or with status 404 (Not Found)
     */
    @GetMapping("/language-locals/{id}")
    public ResponseEntity<LanguageLocale> getLanguageLocal(@PathVariable Long id) {
        log.debug("REST request to get LanguageLocale : {}", id);
        Optional<LanguageLocale> languageLocal = languageLocalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(languageLocal);
    }
}
