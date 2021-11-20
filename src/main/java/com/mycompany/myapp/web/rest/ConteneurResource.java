package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Conteneur;
import com.mycompany.myapp.repository.ConteneurRepository;
import com.mycompany.myapp.service.ConteneurQueryService;
import com.mycompany.myapp.service.ConteneurService;
import com.mycompany.myapp.service.criteria.ConteneurCriteria;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Conteneur}.
 */
@RestController
@RequestMapping("/api")
public class ConteneurResource {

    private final Logger log = LoggerFactory.getLogger(ConteneurResource.class);

    private static final String ENTITY_NAME = "conteneur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConteneurService conteneurService;

    private final ConteneurRepository conteneurRepository;

    private final ConteneurQueryService conteneurQueryService;

    public ConteneurResource(
        ConteneurService conteneurService,
        ConteneurRepository conteneurRepository,
        ConteneurQueryService conteneurQueryService
    ) {
        this.conteneurService = conteneurService;
        this.conteneurRepository = conteneurRepository;
        this.conteneurQueryService = conteneurQueryService;
    }

    /**
     * {@code POST  /conteneurs} : Create a new conteneur.
     *
     * @param conteneur the conteneur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new conteneur, or with status {@code 400 (Bad Request)} if the conteneur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/conteneurs")
    public ResponseEntity<Conteneur> createConteneur(@RequestBody Conteneur conteneur) throws URISyntaxException {
        log.debug("REST request to save Conteneur : {}", conteneur);
        if (conteneur.getId() != null) {
            throw new BadRequestAlertException("A new conteneur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Conteneur result = conteneurService.save(conteneur);
        return ResponseEntity
            .created(new URI("/api/conteneurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /conteneurs/:id} : Updates an existing conteneur.
     *
     * @param id the id of the conteneur to save.
     * @param conteneur the conteneur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conteneur,
     * or with status {@code 400 (Bad Request)} if the conteneur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the conteneur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/conteneurs/{id}")
    public ResponseEntity<Conteneur> updateConteneur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Conteneur conteneur
    ) throws URISyntaxException {
        log.debug("REST request to update Conteneur : {}, {}", id, conteneur);
        if (conteneur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conteneur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conteneurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Conteneur result = conteneurService.save(conteneur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conteneur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /conteneurs/:id} : Partial updates given fields of an existing conteneur, field will ignore if it is null
     *
     * @param id the id of the conteneur to save.
     * @param conteneur the conteneur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated conteneur,
     * or with status {@code 400 (Bad Request)} if the conteneur is not valid,
     * or with status {@code 404 (Not Found)} if the conteneur is not found,
     * or with status {@code 500 (Internal Server Error)} if the conteneur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/conteneurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Conteneur> partialUpdateConteneur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Conteneur conteneur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Conteneur partially : {}, {}", id, conteneur);
        if (conteneur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, conteneur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!conteneurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Conteneur> result = conteneurService.partialUpdate(conteneur);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, conteneur.getId().toString())
        );
    }

    /**
     * {@code GET  /conteneurs} : get all the conteneurs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of conteneurs in body.
     */
    @GetMapping("/conteneurs")
    public ResponseEntity<List<Conteneur>> getAllConteneurs(ConteneurCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Conteneurs by criteria: {}", criteria);
        Page<Conteneur> page = conteneurQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conteneurs/count} : count all the conteneurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/conteneurs/count")
    public ResponseEntity<Long> countConteneurs(ConteneurCriteria criteria) {
        log.debug("REST request to count Conteneurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(conteneurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conteneurs/:id} : get the "id" conteneur.
     *
     * @param id the id of the conteneur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the conteneur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/conteneurs/{id}")
    public ResponseEntity<Conteneur> getConteneur(@PathVariable Long id) {
        log.debug("REST request to get Conteneur : {}", id);
        Optional<Conteneur> conteneur = conteneurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(conteneur);
    }

    /**
     * {@code DELETE  /conteneurs/:id} : delete the "id" conteneur.
     *
     * @param id the id of the conteneur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/conteneurs/{id}")
    public ResponseEntity<Void> deleteConteneur(@PathVariable Long id) {
        log.debug("REST request to delete Conteneur : {}", id);
        conteneurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
