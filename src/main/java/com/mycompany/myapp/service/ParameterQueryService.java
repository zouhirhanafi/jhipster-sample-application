package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Parameter;
import com.mycompany.myapp.repository.ParameterRepository;
import com.mycompany.myapp.service.criteria.ParameterCriteria;
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
 * Service for executing complex queries for {@link Parameter} entities in the database.
 * The main input is a {@link ParameterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Parameter} or a {@link Page} of {@link Parameter} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParameterQueryService extends QueryService<Parameter> {

    private final Logger log = LoggerFactory.getLogger(ParameterQueryService.class);

    private final ParameterRepository parameterRepository;

    public ParameterQueryService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    /**
     * Return a {@link List} of {@link Parameter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Parameter> findByCriteria(ParameterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Parameter> specification = createSpecification(criteria);
        return parameterRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Parameter} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Parameter> findByCriteria(ParameterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Parameter> specification = createSpecification(criteria);
        return parameterRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParameterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Parameter> specification = createSpecification(criteria);
        return parameterRepository.count(specification);
    }

    /**
     * Function to convert {@link ParameterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Parameter> createSpecification(ParameterCriteria criteria) {
        Specification<Parameter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Parameter_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Parameter_.label));
            }
            if (criteria.getActivated() != null) {
                specification = specification.and(buildSpecification(criteria.getActivated(), Parameter_.activated));
            }
            if (criteria.getLib2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLib2(), Parameter_.lib2));
            }
            if (criteria.getLib3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLib3(), Parameter_.lib3));
            }
            if (criteria.getRefExterne() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRefExterne(), Parameter_.refExterne));
            }
            if (criteria.getVal1() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVal1(), Parameter_.val1));
            }
            if (criteria.getVal2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVal2(), Parameter_.val2));
            }
            if (criteria.getVal3() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVal3(), Parameter_.val3));
            }
            if (criteria.getOrdre() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrdre(), Parameter_.ordre));
            }
            if (criteria.getTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTypeId(), root -> root.join(Parameter_.type, JoinType.LEFT).get(Parameter_.id))
                    );
            }
            if (criteria.getParaentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getParaentId(), root -> root.join(Parameter_.paraent, JoinType.LEFT).get(Parameter_.id))
                    );
            }
        }
        return specification;
    }
}
