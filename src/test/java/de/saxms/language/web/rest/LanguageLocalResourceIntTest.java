package de.saxms.language.web.rest;

import de.saxms.language.LocalServerApp;

import de.saxms.language.domain.LanguageLocal;
import de.saxms.language.repository.LanguageLocalRepository;
import de.saxms.language.service.LanguageLocalService;
import de.saxms.language.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static de.saxms.language.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LanguageLocalResource REST controller.
 *
 * @see LanguageLocalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocalServerApp.class)
public class LanguageLocalResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ABBREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABBREVIATION = "BBBBBBBBBB";

    @Autowired
    private LanguageLocalRepository languageLocalRepository;

    @Autowired
    private LanguageLocalService languageLocalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLanguageLocalMockMvc;

    private LanguageLocal languageLocal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LanguageLocalResource languageLocalResource = new LanguageLocalResource(languageLocalService);
        this.restLanguageLocalMockMvc = MockMvcBuilders.standaloneSetup(languageLocalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LanguageLocal createEntity(EntityManager em) {
        LanguageLocal languageLocal = new LanguageLocal()
            .name(DEFAULT_NAME)
            .abbreviation(DEFAULT_ABBREVIATION);
        return languageLocal;
    }

    @Before
    public void initTest() {
        languageLocal = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguageLocal() throws Exception {
        int databaseSizeBeforeCreate = languageLocalRepository.findAll().size();

        // Create the LanguageLocal
        restLanguageLocalMockMvc.perform(post("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLocal)))
            .andExpect(status().isCreated());

        // Validate the LanguageLocal in the database
        List<LanguageLocal> languageLocalList = languageLocalRepository.findAll();
        assertThat(languageLocalList).hasSize(databaseSizeBeforeCreate + 1);
        LanguageLocal testLanguageLocal = languageLocalList.get(languageLocalList.size() - 1);
        assertThat(testLanguageLocal.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLanguageLocal.getAbbreviation()).isEqualTo(DEFAULT_ABBREVIATION);
    }

    @Test
    @Transactional
    public void createLanguageLocalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = languageLocalRepository.findAll().size();

        // Create the LanguageLocal with an existing ID
        languageLocal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageLocalMockMvc.perform(post("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLocal)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageLocal in the database
        List<LanguageLocal> languageLocalList = languageLocalRepository.findAll();
        assertThat(languageLocalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLanguageLocals() throws Exception {
        // Initialize the database
        languageLocalRepository.saveAndFlush(languageLocal);

        // Get all the languageLocalList
        restLanguageLocalMockMvc.perform(get("/api/language-locals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languageLocal.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].abbreviation").value(hasItem(DEFAULT_ABBREVIATION.toString())));
    }
    
    @Test
    @Transactional
    public void getLanguageLocal() throws Exception {
        // Initialize the database
        languageLocalRepository.saveAndFlush(languageLocal);

        // Get the languageLocal
        restLanguageLocalMockMvc.perform(get("/api/language-locals/{id}", languageLocal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(languageLocal.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.abbreviation").value(DEFAULT_ABBREVIATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguageLocal() throws Exception {
        // Get the languageLocal
        restLanguageLocalMockMvc.perform(get("/api/language-locals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguageLocal() throws Exception {
        // Initialize the database
        languageLocalService.save(languageLocal);

        int databaseSizeBeforeUpdate = languageLocalRepository.findAll().size();

        // Update the languageLocal
        LanguageLocal updatedLanguageLocal = languageLocalRepository.findById(languageLocal.getId()).get();
        // Disconnect from session so that the updates on updatedLanguageLocal are not directly saved in db
        em.detach(updatedLanguageLocal);
        updatedLanguageLocal
            .name(UPDATED_NAME)
            .abbreviation(UPDATED_ABBREVIATION);

        restLanguageLocalMockMvc.perform(put("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLanguageLocal)))
            .andExpect(status().isOk());

        // Validate the LanguageLocal in the database
        List<LanguageLocal> languageLocalList = languageLocalRepository.findAll();
        assertThat(languageLocalList).hasSize(databaseSizeBeforeUpdate);
        LanguageLocal testLanguageLocal = languageLocalList.get(languageLocalList.size() - 1);
        assertThat(testLanguageLocal.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLanguageLocal.getAbbreviation()).isEqualTo(UPDATED_ABBREVIATION);
    }

    @Test
    @Transactional
    public void updateNonExistingLanguageLocal() throws Exception {
        int databaseSizeBeforeUpdate = languageLocalRepository.findAll().size();

        // Create the LanguageLocal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageLocalMockMvc.perform(put("/api/language-locals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languageLocal)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageLocal in the database
        List<LanguageLocal> languageLocalList = languageLocalRepository.findAll();
        assertThat(languageLocalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLanguageLocal() throws Exception {
        // Initialize the database
        languageLocalService.save(languageLocal);

        int databaseSizeBeforeDelete = languageLocalRepository.findAll().size();

        // Delete the languageLocal
        restLanguageLocalMockMvc.perform(delete("/api/language-locals/{id}", languageLocal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LanguageLocal> languageLocalList = languageLocalRepository.findAll();
        assertThat(languageLocalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageLocal.class);
        LanguageLocal languageLocal1 = new LanguageLocal();
        languageLocal1.setId(1L);
        LanguageLocal languageLocal2 = new LanguageLocal();
        languageLocal2.setId(languageLocal1.getId());
        assertThat(languageLocal1).isEqualTo(languageLocal2);
        languageLocal2.setId(2L);
        assertThat(languageLocal1).isNotEqualTo(languageLocal2);
        languageLocal1.setId(null);
        assertThat(languageLocal1).isNotEqualTo(languageLocal2);
    }
}
