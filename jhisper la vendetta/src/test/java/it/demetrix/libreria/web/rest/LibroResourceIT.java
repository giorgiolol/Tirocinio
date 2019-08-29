package it.demetrix.libreria.web.rest;

import it.demetrix.libreria.LibreriaApp;
import it.demetrix.libreria.domain.Libro;
import it.demetrix.libreria.repository.LibroRepository;
import it.demetrix.libreria.service.LibroService;
import it.demetrix.libreria.web.rest.errors.ExceptionTranslator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.security.Principal;
import java.util.List;

import static it.demetrix.libreria.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
/**
 * Integration tests for the {@link LibroResource} REST controller.
 */

@SpringBootTest(classes = LibreriaApp.class)

    public class LibroResourceIT {

    private static final String DEFAULT_TITOLO = "AAAAAAAAAA";
    private static final String UPDATED_TITOLO = "BBBBBBBBBB";

    private static final String DEFAULT_AUTORE = "AAAAAAAAAA";
    private static final String UPDATED_AUTORE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ANNO = 1;
    private static final Integer UPDATED_ANNO = 2;
    private static final Integer SMALLER_ANNO = 1 - 1;


    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private LibroService libroService;

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
    @Autowired
    FilterChainProxy springSecurityFilterChain;

    private MockMvc restLibroMockMvc;

    private Libro libro;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        //Log log = LogFactory.getLog(LibroResourceIT.class);
        //log.info(springSecurityFilterChain);
        final LibroResource libroResource = new LibroResource(libroService);
        this.restLibroMockMvc = MockMvcBuilders.standaloneSetup(libroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .apply(SecurityMockMvcConfigurers.springSecurity(springSecurityFilterChain))
            .setValidator(validator)
            .build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createEntity(EntityManager em) {
        Libro libro = new Libro()
            .titolo(DEFAULT_TITOLO)
            .autore(DEFAULT_AUTORE)
            .anno(DEFAULT_ANNO);
        return libro;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Libro createUpdatedEntity(EntityManager em) {
        Libro libro = new Libro()
            .titolo(UPDATED_TITOLO)
            .autore(UPDATED_AUTORE)
            .anno(UPDATED_ANNO);
        return libro;
    }

    @BeforeEach
    public void initTest() {
        libro = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_USER"})
    public void createLibro() throws Exception {
        int databaseSizeBeforeCreate = libroRepository.findAll().size();

        // Create the Libro
        restLibroMockMvc.perform(post("/api/libros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(libro)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isCreated());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeCreate + 1);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getTitolo()).isEqualTo(DEFAULT_TITOLO);
        assertThat(testLibro.getAutore()).isEqualTo(DEFAULT_AUTORE);
        assertThat(testLibro.getAnno()).isEqualTo(DEFAULT_ANNO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_USER"})
    public void createLibroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = libroRepository.findAll().size();

        // Create the Libro with an existing ID
        libro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLibroMockMvc.perform(post("/api/libros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(libro)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_USER"})
    public void getAllLibros() throws Exception {
        // Initialize the database
       // libroRepository.saveAndFlush(libro);

        // Get all the libroList
        restLibroMockMvc.perform(get("/api/libros?sort=id,desc"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));

    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_ADMIN"})
    public void getLibro() throws Exception {
        // Initialize the database
       // libroRepository.saveAndFlush(libro);

        // Get the libro
        restLibroMockMvc.perform(get("/api/libros/{id}", libro.getId()))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_ADMIN"})
    public void getNonExistingLibro() throws Exception {
        // Get the libro
        restLibroMockMvc.perform(get("/api/libros/{id}", Long.MAX_VALUE))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_ADMIN"})
    public void updateLibroAdmin() throws Exception {
        // Initialize the database
        libroService.save(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro
        Libro updatedLibro = libroRepository.findById(libro.getId()).get();
        // Disconnect from session so that the updates on updatedLibro are not directly saved in db
        em.detach(updatedLibro);
        updatedLibro
            .titolo(UPDATED_TITOLO)
            .autore(UPDATED_AUTORE)
            .anno(UPDATED_ANNO);

        restLibroMockMvc.perform(put("/api/libros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLibro)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getTitolo()).isEqualTo(UPDATED_TITOLO);
        assertThat(testLibro.getAutore()).isEqualTo(UPDATED_AUTORE);
        assertThat(testLibro.getAnno()).isEqualTo(UPDATED_ANNO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "user",password = "user",authorities={"ROLE_EDITOR"})
    public void updateLibroEditor() throws Exception {
        // Initialize the database
        libroService.save(libro);

        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Update the libro
        Libro updatedLibro = libroRepository.findById(libro.getId()).get();
        // Disconnect from session so that the updates on updatedLibro are not directly saved in db
        em.detach(updatedLibro);
        updatedLibro
            .titolo(UPDATED_TITOLO)
            .autore(UPDATED_AUTORE)
            .anno(UPDATED_ANNO);

        restLibroMockMvc.perform(put("/api/libros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLibro)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
        Libro testLibro = libroList.get(libroList.size() - 1);
        assertThat(testLibro.getTitolo()).isEqualTo(UPDATED_TITOLO);
        assertThat(testLibro.getAutore()).isEqualTo(UPDATED_AUTORE);
        assertThat(testLibro.getAnno()).isEqualTo(UPDATED_ANNO);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_ADMIN"})
    public void updateNonExistingLibro() throws Exception {
        int databaseSizeBeforeUpdate = libroRepository.findAll().size();

        // Create the Libro

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLibroMockMvc.perform(put("/api/libros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(libro)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest());

        // Validate the Libro in the database
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    @WithMockUser(username = "admin",password = "admin",authorities={"ROLE_ADMIN"})
    public void deleteLibro() throws Exception {
        // Initialize the database
        libroService.save(libro);

        int databaseSizeBeforeDelete = libroRepository.findAll().size();

        // Delete the libro
        restLibroMockMvc.perform(delete("/api/libros/{id}", libro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    @WithMockUser(username = "users",password = "users",authorities={"ROLE_USER"})
    public void deleteLibroUser() throws Exception {
        // Initialize the database
        libroService.save(libro);
        int databaseSizeBeforeDelete = libroRepository.findAll().size();

        // Delete the libro
        restLibroMockMvc.perform(delete("/api/libros/{id}", libro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden());

        // Validate the database contains one less item
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    @WithMockUser(username = "users",password = "users",authorities={"ROLE_USER"})
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Libro.class);
        Libro libro1 = new Libro();
        libro1.setId(1L);
        Libro libro2 = new Libro();
        libro2.setId(libro1.getId());
        assertThat(libro1).isEqualTo(libro2);
        libro2.setId(2L);
        assertThat(libro1).isNotEqualTo(libro2);
        libro1.setId(null);
        assertThat(libro1).isNotEqualTo(libro2);
    }

    @Test
    @Transactional
    @WithMockUser(username = "user",password = "user",authorities={"ROLE_USER"})
    public void seeLibriByAutore () throws Exception{

        restLibroMockMvc.perform(get("/api/libros/you"))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    public void seeLibriByAutoreUnauthorized () throws Exception{

        restLibroMockMvc.perform(get("/api/libros/you"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    @Transactional
    @WithMockUser(username = "users",password = "users",authorities={"ROLE_EDITOR"})
    public void deleteLibroEditor() throws Exception {
        // Initialize the database
        libroService.save(libro);
        int databaseSizeBeforeDelete = libroRepository.findAll().size();

        // Delete the libro
        restLibroMockMvc.perform(delete("/api/libros/{id}", libro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andDo(MockMvcResultHandlers.print())
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isForbidden());

        // Validate the database contains one less item
        List<Libro> libroList = libroRepository.findAll();
        assertThat(libroList).hasSize(databaseSizeBeforeDelete);
    }


}
