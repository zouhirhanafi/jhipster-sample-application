package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Conteneur;
import com.mycompany.myapp.repository.ConteneurRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Conteneur}.
 */
@Service
@Transactional
public class ConteneurService {

    private final Logger log = LoggerFactory.getLogger(ConteneurService.class);

    private final ConteneurRepository conteneurRepository;

    public ConteneurService(ConteneurRepository conteneurRepository) {
        this.conteneurRepository = conteneurRepository;
    }

    /**
     * Save a conteneur.
     *
     * @param conteneur the entity to save.
     * @return the persisted entity.
     */
    public Conteneur save(Conteneur conteneur) {
        log.debug("Request to save Conteneur : {}", conteneur);
        return conteneurRepository.save(conteneur);
    }

    /**
     * Partially update a conteneur.
     *
     * @param conteneur the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Conteneur> partialUpdate(Conteneur conteneur) {
        log.debug("Request to partially update Conteneur : {}", conteneur);

        return conteneurRepository
            .findById(conteneur.getId())
            .map(existingConteneur -> {
                if (conteneur.getStatut() != null) {
                    existingConteneur.setStatut(conteneur.getStatut());
                }
                if (conteneur.getDateEntree() != null) {
                    existingConteneur.setDateEntree(conteneur.getDateEntree());
                }
                if (conteneur.getDateSortie() != null) {
                    existingConteneur.setDateSortie(conteneur.getDateSortie());
                }
                if (conteneur.getZone() != null) {
                    existingConteneur.setZone(conteneur.getZone());
                }
                if (conteneur.getLigne() != null) {
                    existingConteneur.setLigne(conteneur.getLigne());
                }
                if (conteneur.getColonne() != null) {
                    existingConteneur.setColonne(conteneur.getColonne());
                }
                if (conteneur.getCommentaire() != null) {
                    existingConteneur.setCommentaire(conteneur.getCommentaire());
                }

                return existingConteneur;
            })
            .map(conteneurRepository::save);
    }

    /**
     * Get all the conteneurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Conteneur> findAll(Pageable pageable) {
        log.debug("Request to get all Conteneurs");
        return conteneurRepository.findAll(pageable);
    }

    /**
     * Get one conteneur by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Conteneur> findOne(Long id) {
        log.debug("Request to get Conteneur : {}", id);
        return conteneurRepository.findById(id);
    }

    /**
     * Delete the conteneur by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Conteneur : {}", id);
        conteneurRepository.deleteById(id);
    }
}
