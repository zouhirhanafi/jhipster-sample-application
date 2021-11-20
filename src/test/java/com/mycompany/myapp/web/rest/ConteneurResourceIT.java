package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Conteneur;
import com.mycompany.myapp.repository.ConteneurRepository;
import com.mycompany.myapp.service.criteria.ConteneurCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConteneurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConteneurResourceIT {

    private static final Integer DEFAULT_STATUT = 1;
    private static final Integer UPDATED_STATUT = 2;
    private static final Integer SMALLER_STATUT = 1 - 1;

    private static final Instant DEFAULT_DATE_ENTREE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ENTREE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ZonedDateTime DEFAULT_DATE_SORTIE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_SORTIE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE_SORTIE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Integer DEFAULT_ZONE = 1;
    private static final Integer UPDATED_ZONE = 2;
    private static final Integer SMALLER_ZONE = 1 - 1;

    private static final Integer DEFAULT_LIGNE = 1;
    private static final Integer UPDATED_LIGNE = 2;
    private static final Integer SMALLER_LIGNE = 1 - 1;

    private static final Integer DEFAULT_COLONNE = 1;
    private static final Integer UPDATED_COLONNE = 2;
    private static final Integer SMALLER_COLONNE = 1 - 1;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/conteneurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConteneurRepository conteneurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConteneurMockMvc;

    private Conteneur conteneur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conteneur createEntity(EntityManager em) {
        Conteneur conteneur = new Conteneur()
            .statut(DEFAULT_STATUT)
            .dateEntree(DEFAULT_DATE_ENTREE)
            .dateSortie(DEFAULT_DATE_SORTIE)
            .zone(DEFAULT_ZONE)
            .ligne(DEFAULT_LIGNE)
            .colonne(DEFAULT_COLONNE)
            .commentaire(DEFAULT_COMMENTAIRE);
        return conteneur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conteneur createUpdatedEntity(EntityManager em) {
        Conteneur conteneur = new Conteneur()
            .statut(UPDATED_STATUT)
            .dateEntree(UPDATED_DATE_ENTREE)
            .dateSortie(UPDATED_DATE_SORTIE)
            .zone(UPDATED_ZONE)
            .ligne(UPDATED_LIGNE)
            .colonne(UPDATED_COLONNE)
            .commentaire(UPDATED_COMMENTAIRE);
        return conteneur;
    }

    @BeforeEach
    public void initTest() {
        conteneur = createEntity(em);
    }

    @Test
    @Transactional
    void createConteneur() throws Exception {
        int databaseSizeBeforeCreate = conteneurRepository.findAll().size();
        // Create the Conteneur
        restConteneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conteneur)))
            .andExpect(status().isCreated());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeCreate + 1);
        Conteneur testConteneur = conteneurList.get(conteneurList.size() - 1);
        assertThat(testConteneur.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testConteneur.getDateEntree()).isEqualTo(DEFAULT_DATE_ENTREE);
        assertThat(testConteneur.getDateSortie()).isEqualTo(DEFAULT_DATE_SORTIE);
        assertThat(testConteneur.getZone()).isEqualTo(DEFAULT_ZONE);
        assertThat(testConteneur.getLigne()).isEqualTo(DEFAULT_LIGNE);
        assertThat(testConteneur.getColonne()).isEqualTo(DEFAULT_COLONNE);
        assertThat(testConteneur.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    void createConteneurWithExistingId() throws Exception {
        // Create the Conteneur with an existing ID
        conteneur.setId(1L);

        int databaseSizeBeforeCreate = conteneurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConteneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conteneur)))
            .andExpect(status().isBadRequest());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConteneurs() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList
        restConteneurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conteneur.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].dateEntree").value(hasItem(DEFAULT_DATE_ENTREE.toString())))
            .andExpect(jsonPath("$.[*].dateSortie").value(hasItem(sameInstant(DEFAULT_DATE_SORTIE))))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE)))
            .andExpect(jsonPath("$.[*].ligne").value(hasItem(DEFAULT_LIGNE)))
            .andExpect(jsonPath("$.[*].colonne").value(hasItem(DEFAULT_COLONNE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));
    }

    @Test
    @Transactional
    void getConteneur() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get the conteneur
        restConteneurMockMvc
            .perform(get(ENTITY_API_URL_ID, conteneur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conteneur.getId().intValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.dateEntree").value(DEFAULT_DATE_ENTREE.toString()))
            .andExpect(jsonPath("$.dateSortie").value(sameInstant(DEFAULT_DATE_SORTIE)))
            .andExpect(jsonPath("$.zone").value(DEFAULT_ZONE))
            .andExpect(jsonPath("$.ligne").value(DEFAULT_LIGNE))
            .andExpect(jsonPath("$.colonne").value(DEFAULT_COLONNE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE));
    }

    @Test
    @Transactional
    void getConteneursByIdFiltering() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        Long id = conteneur.getId();

        defaultConteneurShouldBeFound("id.equals=" + id);
        defaultConteneurShouldNotBeFound("id.notEquals=" + id);

        defaultConteneurShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultConteneurShouldNotBeFound("id.greaterThan=" + id);

        defaultConteneurShouldBeFound("id.lessThanOrEqual=" + id);
        defaultConteneurShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut equals to DEFAULT_STATUT
        defaultConteneurShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the conteneurList where statut equals to UPDATED_STATUT
        defaultConteneurShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut not equals to DEFAULT_STATUT
        defaultConteneurShouldNotBeFound("statut.notEquals=" + DEFAULT_STATUT);

        // Get all the conteneurList where statut not equals to UPDATED_STATUT
        defaultConteneurShouldBeFound("statut.notEquals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultConteneurShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the conteneurList where statut equals to UPDATED_STATUT
        defaultConteneurShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut is not null
        defaultConteneurShouldBeFound("statut.specified=true");

        // Get all the conteneurList where statut is null
        defaultConteneurShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut is greater than or equal to DEFAULT_STATUT
        defaultConteneurShouldBeFound("statut.greaterThanOrEqual=" + DEFAULT_STATUT);

        // Get all the conteneurList where statut is greater than or equal to UPDATED_STATUT
        defaultConteneurShouldNotBeFound("statut.greaterThanOrEqual=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut is less than or equal to DEFAULT_STATUT
        defaultConteneurShouldBeFound("statut.lessThanOrEqual=" + DEFAULT_STATUT);

        // Get all the conteneurList where statut is less than or equal to SMALLER_STATUT
        defaultConteneurShouldNotBeFound("statut.lessThanOrEqual=" + SMALLER_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsLessThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut is less than DEFAULT_STATUT
        defaultConteneurShouldNotBeFound("statut.lessThan=" + DEFAULT_STATUT);

        // Get all the conteneurList where statut is less than UPDATED_STATUT
        defaultConteneurShouldBeFound("statut.lessThan=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByStatutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where statut is greater than DEFAULT_STATUT
        defaultConteneurShouldNotBeFound("statut.greaterThan=" + DEFAULT_STATUT);

        // Get all the conteneurList where statut is greater than SMALLER_STATUT
        defaultConteneurShouldBeFound("statut.greaterThan=" + SMALLER_STATUT);
    }

    @Test
    @Transactional
    void getAllConteneursByDateEntreeIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateEntree equals to DEFAULT_DATE_ENTREE
        defaultConteneurShouldBeFound("dateEntree.equals=" + DEFAULT_DATE_ENTREE);

        // Get all the conteneurList where dateEntree equals to UPDATED_DATE_ENTREE
        defaultConteneurShouldNotBeFound("dateEntree.equals=" + UPDATED_DATE_ENTREE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateEntreeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateEntree not equals to DEFAULT_DATE_ENTREE
        defaultConteneurShouldNotBeFound("dateEntree.notEquals=" + DEFAULT_DATE_ENTREE);

        // Get all the conteneurList where dateEntree not equals to UPDATED_DATE_ENTREE
        defaultConteneurShouldBeFound("dateEntree.notEquals=" + UPDATED_DATE_ENTREE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateEntreeIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateEntree in DEFAULT_DATE_ENTREE or UPDATED_DATE_ENTREE
        defaultConteneurShouldBeFound("dateEntree.in=" + DEFAULT_DATE_ENTREE + "," + UPDATED_DATE_ENTREE);

        // Get all the conteneurList where dateEntree equals to UPDATED_DATE_ENTREE
        defaultConteneurShouldNotBeFound("dateEntree.in=" + UPDATED_DATE_ENTREE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateEntreeIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateEntree is not null
        defaultConteneurShouldBeFound("dateEntree.specified=true");

        // Get all the conteneurList where dateEntree is null
        defaultConteneurShouldNotBeFound("dateEntree.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie equals to DEFAULT_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.equals=" + DEFAULT_DATE_SORTIE);

        // Get all the conteneurList where dateSortie equals to UPDATED_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.equals=" + UPDATED_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie not equals to DEFAULT_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.notEquals=" + DEFAULT_DATE_SORTIE);

        // Get all the conteneurList where dateSortie not equals to UPDATED_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.notEquals=" + UPDATED_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie in DEFAULT_DATE_SORTIE or UPDATED_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.in=" + DEFAULT_DATE_SORTIE + "," + UPDATED_DATE_SORTIE);

        // Get all the conteneurList where dateSortie equals to UPDATED_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.in=" + UPDATED_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie is not null
        defaultConteneurShouldBeFound("dateSortie.specified=true");

        // Get all the conteneurList where dateSortie is null
        defaultConteneurShouldNotBeFound("dateSortie.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie is greater than or equal to DEFAULT_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.greaterThanOrEqual=" + DEFAULT_DATE_SORTIE);

        // Get all the conteneurList where dateSortie is greater than or equal to UPDATED_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.greaterThanOrEqual=" + UPDATED_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie is less than or equal to DEFAULT_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.lessThanOrEqual=" + DEFAULT_DATE_SORTIE);

        // Get all the conteneurList where dateSortie is less than or equal to SMALLER_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.lessThanOrEqual=" + SMALLER_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsLessThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie is less than DEFAULT_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.lessThan=" + DEFAULT_DATE_SORTIE);

        // Get all the conteneurList where dateSortie is less than UPDATED_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.lessThan=" + UPDATED_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByDateSortieIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where dateSortie is greater than DEFAULT_DATE_SORTIE
        defaultConteneurShouldNotBeFound("dateSortie.greaterThan=" + DEFAULT_DATE_SORTIE);

        // Get all the conteneurList where dateSortie is greater than SMALLER_DATE_SORTIE
        defaultConteneurShouldBeFound("dateSortie.greaterThan=" + SMALLER_DATE_SORTIE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone equals to DEFAULT_ZONE
        defaultConteneurShouldBeFound("zone.equals=" + DEFAULT_ZONE);

        // Get all the conteneurList where zone equals to UPDATED_ZONE
        defaultConteneurShouldNotBeFound("zone.equals=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone not equals to DEFAULT_ZONE
        defaultConteneurShouldNotBeFound("zone.notEquals=" + DEFAULT_ZONE);

        // Get all the conteneurList where zone not equals to UPDATED_ZONE
        defaultConteneurShouldBeFound("zone.notEquals=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone in DEFAULT_ZONE or UPDATED_ZONE
        defaultConteneurShouldBeFound("zone.in=" + DEFAULT_ZONE + "," + UPDATED_ZONE);

        // Get all the conteneurList where zone equals to UPDATED_ZONE
        defaultConteneurShouldNotBeFound("zone.in=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone is not null
        defaultConteneurShouldBeFound("zone.specified=true");

        // Get all the conteneurList where zone is null
        defaultConteneurShouldNotBeFound("zone.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone is greater than or equal to DEFAULT_ZONE
        defaultConteneurShouldBeFound("zone.greaterThanOrEqual=" + DEFAULT_ZONE);

        // Get all the conteneurList where zone is greater than or equal to UPDATED_ZONE
        defaultConteneurShouldNotBeFound("zone.greaterThanOrEqual=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone is less than or equal to DEFAULT_ZONE
        defaultConteneurShouldBeFound("zone.lessThanOrEqual=" + DEFAULT_ZONE);

        // Get all the conteneurList where zone is less than or equal to SMALLER_ZONE
        defaultConteneurShouldNotBeFound("zone.lessThanOrEqual=" + SMALLER_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsLessThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone is less than DEFAULT_ZONE
        defaultConteneurShouldNotBeFound("zone.lessThan=" + DEFAULT_ZONE);

        // Get all the conteneurList where zone is less than UPDATED_ZONE
        defaultConteneurShouldBeFound("zone.lessThan=" + UPDATED_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByZoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where zone is greater than DEFAULT_ZONE
        defaultConteneurShouldNotBeFound("zone.greaterThan=" + DEFAULT_ZONE);

        // Get all the conteneurList where zone is greater than SMALLER_ZONE
        defaultConteneurShouldBeFound("zone.greaterThan=" + SMALLER_ZONE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne equals to DEFAULT_LIGNE
        defaultConteneurShouldBeFound("ligne.equals=" + DEFAULT_LIGNE);

        // Get all the conteneurList where ligne equals to UPDATED_LIGNE
        defaultConteneurShouldNotBeFound("ligne.equals=" + UPDATED_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne not equals to DEFAULT_LIGNE
        defaultConteneurShouldNotBeFound("ligne.notEquals=" + DEFAULT_LIGNE);

        // Get all the conteneurList where ligne not equals to UPDATED_LIGNE
        defaultConteneurShouldBeFound("ligne.notEquals=" + UPDATED_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne in DEFAULT_LIGNE or UPDATED_LIGNE
        defaultConteneurShouldBeFound("ligne.in=" + DEFAULT_LIGNE + "," + UPDATED_LIGNE);

        // Get all the conteneurList where ligne equals to UPDATED_LIGNE
        defaultConteneurShouldNotBeFound("ligne.in=" + UPDATED_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne is not null
        defaultConteneurShouldBeFound("ligne.specified=true");

        // Get all the conteneurList where ligne is null
        defaultConteneurShouldNotBeFound("ligne.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne is greater than or equal to DEFAULT_LIGNE
        defaultConteneurShouldBeFound("ligne.greaterThanOrEqual=" + DEFAULT_LIGNE);

        // Get all the conteneurList where ligne is greater than or equal to UPDATED_LIGNE
        defaultConteneurShouldNotBeFound("ligne.greaterThanOrEqual=" + UPDATED_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne is less than or equal to DEFAULT_LIGNE
        defaultConteneurShouldBeFound("ligne.lessThanOrEqual=" + DEFAULT_LIGNE);

        // Get all the conteneurList where ligne is less than or equal to SMALLER_LIGNE
        defaultConteneurShouldNotBeFound("ligne.lessThanOrEqual=" + SMALLER_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsLessThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne is less than DEFAULT_LIGNE
        defaultConteneurShouldNotBeFound("ligne.lessThan=" + DEFAULT_LIGNE);

        // Get all the conteneurList where ligne is less than UPDATED_LIGNE
        defaultConteneurShouldBeFound("ligne.lessThan=" + UPDATED_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByLigneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where ligne is greater than DEFAULT_LIGNE
        defaultConteneurShouldNotBeFound("ligne.greaterThan=" + DEFAULT_LIGNE);

        // Get all the conteneurList where ligne is greater than SMALLER_LIGNE
        defaultConteneurShouldBeFound("ligne.greaterThan=" + SMALLER_LIGNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne equals to DEFAULT_COLONNE
        defaultConteneurShouldBeFound("colonne.equals=" + DEFAULT_COLONNE);

        // Get all the conteneurList where colonne equals to UPDATED_COLONNE
        defaultConteneurShouldNotBeFound("colonne.equals=" + UPDATED_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne not equals to DEFAULT_COLONNE
        defaultConteneurShouldNotBeFound("colonne.notEquals=" + DEFAULT_COLONNE);

        // Get all the conteneurList where colonne not equals to UPDATED_COLONNE
        defaultConteneurShouldBeFound("colonne.notEquals=" + UPDATED_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne in DEFAULT_COLONNE or UPDATED_COLONNE
        defaultConteneurShouldBeFound("colonne.in=" + DEFAULT_COLONNE + "," + UPDATED_COLONNE);

        // Get all the conteneurList where colonne equals to UPDATED_COLONNE
        defaultConteneurShouldNotBeFound("colonne.in=" + UPDATED_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne is not null
        defaultConteneurShouldBeFound("colonne.specified=true");

        // Get all the conteneurList where colonne is null
        defaultConteneurShouldNotBeFound("colonne.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne is greater than or equal to DEFAULT_COLONNE
        defaultConteneurShouldBeFound("colonne.greaterThanOrEqual=" + DEFAULT_COLONNE);

        // Get all the conteneurList where colonne is greater than or equal to UPDATED_COLONNE
        defaultConteneurShouldNotBeFound("colonne.greaterThanOrEqual=" + UPDATED_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne is less than or equal to DEFAULT_COLONNE
        defaultConteneurShouldBeFound("colonne.lessThanOrEqual=" + DEFAULT_COLONNE);

        // Get all the conteneurList where colonne is less than or equal to SMALLER_COLONNE
        defaultConteneurShouldNotBeFound("colonne.lessThanOrEqual=" + SMALLER_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsLessThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne is less than DEFAULT_COLONNE
        defaultConteneurShouldNotBeFound("colonne.lessThan=" + DEFAULT_COLONNE);

        // Get all the conteneurList where colonne is less than UPDATED_COLONNE
        defaultConteneurShouldBeFound("colonne.lessThan=" + UPDATED_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByColonneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where colonne is greater than DEFAULT_COLONNE
        defaultConteneurShouldNotBeFound("colonne.greaterThan=" + DEFAULT_COLONNE);

        // Get all the conteneurList where colonne is greater than SMALLER_COLONNE
        defaultConteneurShouldBeFound("colonne.greaterThan=" + SMALLER_COLONNE);
    }

    @Test
    @Transactional
    void getAllConteneursByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where commentaire equals to DEFAULT_COMMENTAIRE
        defaultConteneurShouldBeFound("commentaire.equals=" + DEFAULT_COMMENTAIRE);

        // Get all the conteneurList where commentaire equals to UPDATED_COMMENTAIRE
        defaultConteneurShouldNotBeFound("commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllConteneursByCommentaireIsNotEqualToSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where commentaire not equals to DEFAULT_COMMENTAIRE
        defaultConteneurShouldNotBeFound("commentaire.notEquals=" + DEFAULT_COMMENTAIRE);

        // Get all the conteneurList where commentaire not equals to UPDATED_COMMENTAIRE
        defaultConteneurShouldBeFound("commentaire.notEquals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllConteneursByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where commentaire in DEFAULT_COMMENTAIRE or UPDATED_COMMENTAIRE
        defaultConteneurShouldBeFound("commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE);

        // Get all the conteneurList where commentaire equals to UPDATED_COMMENTAIRE
        defaultConteneurShouldNotBeFound("commentaire.in=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllConteneursByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where commentaire is not null
        defaultConteneurShouldBeFound("commentaire.specified=true");

        // Get all the conteneurList where commentaire is null
        defaultConteneurShouldNotBeFound("commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllConteneursByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where commentaire contains DEFAULT_COMMENTAIRE
        defaultConteneurShouldBeFound("commentaire.contains=" + DEFAULT_COMMENTAIRE);

        // Get all the conteneurList where commentaire contains UPDATED_COMMENTAIRE
        defaultConteneurShouldNotBeFound("commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllConteneursByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        // Get all the conteneurList where commentaire does not contain DEFAULT_COMMENTAIRE
        defaultConteneurShouldNotBeFound("commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE);

        // Get all the conteneurList where commentaire does not contain UPDATED_COMMENTAIRE
        defaultConteneurShouldBeFound("commentaire.doesNotContain=" + UPDATED_COMMENTAIRE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConteneurShouldBeFound(String filter) throws Exception {
        restConteneurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conteneur.getId().intValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].dateEntree").value(hasItem(DEFAULT_DATE_ENTREE.toString())))
            .andExpect(jsonPath("$.[*].dateSortie").value(hasItem(sameInstant(DEFAULT_DATE_SORTIE))))
            .andExpect(jsonPath("$.[*].zone").value(hasItem(DEFAULT_ZONE)))
            .andExpect(jsonPath("$.[*].ligne").value(hasItem(DEFAULT_LIGNE)))
            .andExpect(jsonPath("$.[*].colonne").value(hasItem(DEFAULT_COLONNE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)));

        // Check, that the count call also returns 1
        restConteneurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConteneurShouldNotBeFound(String filter) throws Exception {
        restConteneurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConteneurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConteneur() throws Exception {
        // Get the conteneur
        restConteneurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConteneur() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();

        // Update the conteneur
        Conteneur updatedConteneur = conteneurRepository.findById(conteneur.getId()).get();
        // Disconnect from session so that the updates on updatedConteneur are not directly saved in db
        em.detach(updatedConteneur);
        updatedConteneur
            .statut(UPDATED_STATUT)
            .dateEntree(UPDATED_DATE_ENTREE)
            .dateSortie(UPDATED_DATE_SORTIE)
            .zone(UPDATED_ZONE)
            .ligne(UPDATED_LIGNE)
            .colonne(UPDATED_COLONNE)
            .commentaire(UPDATED_COMMENTAIRE);

        restConteneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConteneur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConteneur))
            )
            .andExpect(status().isOk());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
        Conteneur testConteneur = conteneurList.get(conteneurList.size() - 1);
        assertThat(testConteneur.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testConteneur.getDateEntree()).isEqualTo(UPDATED_DATE_ENTREE);
        assertThat(testConteneur.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
        assertThat(testConteneur.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testConteneur.getLigne()).isEqualTo(UPDATED_LIGNE);
        assertThat(testConteneur.getColonne()).isEqualTo(UPDATED_COLONNE);
        assertThat(testConteneur.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void putNonExistingConteneur() throws Exception {
        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();
        conteneur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConteneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conteneur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conteneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConteneur() throws Exception {
        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();
        conteneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConteneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(conteneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConteneur() throws Exception {
        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();
        conteneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConteneurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(conteneur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConteneurWithPatch() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();

        // Update the conteneur using partial update
        Conteneur partialUpdatedConteneur = new Conteneur();
        partialUpdatedConteneur.setId(conteneur.getId());

        partialUpdatedConteneur
            .statut(UPDATED_STATUT)
            .dateEntree(UPDATED_DATE_ENTREE)
            .zone(UPDATED_ZONE)
            .ligne(UPDATED_LIGNE)
            .commentaire(UPDATED_COMMENTAIRE);

        restConteneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConteneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConteneur))
            )
            .andExpect(status().isOk());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
        Conteneur testConteneur = conteneurList.get(conteneurList.size() - 1);
        assertThat(testConteneur.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testConteneur.getDateEntree()).isEqualTo(UPDATED_DATE_ENTREE);
        assertThat(testConteneur.getDateSortie()).isEqualTo(DEFAULT_DATE_SORTIE);
        assertThat(testConteneur.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testConteneur.getLigne()).isEqualTo(UPDATED_LIGNE);
        assertThat(testConteneur.getColonne()).isEqualTo(DEFAULT_COLONNE);
        assertThat(testConteneur.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void fullUpdateConteneurWithPatch() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();

        // Update the conteneur using partial update
        Conteneur partialUpdatedConteneur = new Conteneur();
        partialUpdatedConteneur.setId(conteneur.getId());

        partialUpdatedConteneur
            .statut(UPDATED_STATUT)
            .dateEntree(UPDATED_DATE_ENTREE)
            .dateSortie(UPDATED_DATE_SORTIE)
            .zone(UPDATED_ZONE)
            .ligne(UPDATED_LIGNE)
            .colonne(UPDATED_COLONNE)
            .commentaire(UPDATED_COMMENTAIRE);

        restConteneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConteneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConteneur))
            )
            .andExpect(status().isOk());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
        Conteneur testConteneur = conteneurList.get(conteneurList.size() - 1);
        assertThat(testConteneur.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testConteneur.getDateEntree()).isEqualTo(UPDATED_DATE_ENTREE);
        assertThat(testConteneur.getDateSortie()).isEqualTo(UPDATED_DATE_SORTIE);
        assertThat(testConteneur.getZone()).isEqualTo(UPDATED_ZONE);
        assertThat(testConteneur.getLigne()).isEqualTo(UPDATED_LIGNE);
        assertThat(testConteneur.getColonne()).isEqualTo(UPDATED_COLONNE);
        assertThat(testConteneur.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void patchNonExistingConteneur() throws Exception {
        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();
        conteneur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConteneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conteneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conteneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConteneur() throws Exception {
        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();
        conteneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConteneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(conteneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConteneur() throws Exception {
        int databaseSizeBeforeUpdate = conteneurRepository.findAll().size();
        conteneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConteneurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(conteneur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Conteneur in the database
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConteneur() throws Exception {
        // Initialize the database
        conteneurRepository.saveAndFlush(conteneur);

        int databaseSizeBeforeDelete = conteneurRepository.findAll().size();

        // Delete the conteneur
        restConteneurMockMvc
            .perform(delete(ENTITY_API_URL_ID, conteneur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Conteneur> conteneurList = conteneurRepository.findAll();
        assertThat(conteneurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
