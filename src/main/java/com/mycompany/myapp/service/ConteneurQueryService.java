package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Conteneur;
import com.mycompany.myapp.repository.ConteneurRepository;
import com.mycompany.myapp.service.criteria.ConteneurCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Conteneur} entities in the database.
 * The main input is a {@link ConteneurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Conteneur} or a {@link Page} of {@link Conteneur} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConteneurQueryService extends QueryService<Conteneur> {

    private final Logger log = LoggerFactory.getLogger(ConteneurQueryService.class);

    private final ConteneurRepository conteneurRepository;

    public ConteneurQueryService(ConteneurRepository conteneurRepository) {
        this.conteneurRepository = conteneurRepository;
    }

    /**
     * Return a {@link List} of {@link Conteneur} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Conteneur> findByCriteria(ConteneurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Conteneur> specification = createSpecification(criteria);
        return conteneurRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Conteneur} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Conteneur> findByCriteria(ConteneurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Conteneur> specification = createSpecification(criteria);
        return conteneurRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConteneurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Conteneur> specification = createSpecification(criteria);
        return conteneurRepository.count(specification);
    }

    /**
     * Function to convert {@link ConteneurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Conteneur> createSpecification(ConteneurCriteria criteria) {
        Specification<Conteneur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Conteneur_.id));
            }
            if (criteria.getStatut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatut(), Conteneur_.statut));
            }
            if (criteria.getDateEntree() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateEntree(), Conteneur_.dateEntree));
            }
            if (criteria.getDateSortie() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateSortie(), Conteneur_.dateSortie));
            }
            if (criteria.getZone() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getZone(), Conteneur_.zone));
            }
            if (criteria.getLigne() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLigne(), Conteneur_.ligne));
            }
            if (criteria.getColonne() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getColonne(), Conteneur_.colonne));
            }
            if (criteria.getCommentaire() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommentaire(), Conteneur_.commentaire));
            }
        }
        return specification;
    }
}
