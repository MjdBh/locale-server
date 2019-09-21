package de.saxms.language.web.rest;
import de.saxms.language.domain.LanguageLocal;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing LanguageLocal.
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
     * POST  /language-locals : Create a new languageLocal.
     *
     * @param languageLocal the languageLocal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new languageLocal, or with status 400 (Bad Request) if the languageLocal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/language-locals")
    public ResponseEntity<LanguageLocal> createLanguageLocal(@RequestBody LanguageLocal languageLocal) throws URISyntaxException {
        log.debug("REST request to save LanguageLocal : {}", languageLocal);
        if (languageLocal.getId() != null) {
            throw new BadRequestAlertException("A new languageLocal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LanguageLocal result = languageLocalService.save(languageLocal);
        return ResponseEntity.created(new URI("/api/language-locals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /language-locals : Updates an existing languageLocal.
     *
     * @param languageLocal the languageLocal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated languageLocal,
     * or with status 400 (Bad Request) if the languageLocal is not valid,
     * or with status 500 (Internal Server Error) if the languageLocal couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/language-locals")
    public ResponseEntity<LanguageLocal> updateLanguageLocal(@RequestBody LanguageLocal languageLocal) throws URISyntaxException {
        log.debug("REST request to update LanguageLocal : {}", languageLocal);
        if (languageLocal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LanguageLocal result = languageLocalService.save(languageLocal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, languageLocal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /language-locals : get all the languageLocals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of languageLocals in body
     */
    @GetMapping("/language-locals")
    public ResponseEntity<List<LanguageLocal>> getAllLanguageLocals(Pageable pageable) {
        log.debug("REST request to get a page of LanguageLocals");
        Page<LanguageLocal> page = languageLocalService.findAll(pageable);
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
    public ResponseEntity<LanguageLocal> getLanguageLocal(@PathVariable Long id) {
        log.debug("REST request to get LanguageLocal : {}", id);
        Optional<LanguageLocal> languageLocal = languageLocalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(languageLocal);
    }
}
