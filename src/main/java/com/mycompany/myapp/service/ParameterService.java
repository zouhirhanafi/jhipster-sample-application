package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Parameter;
import com.mycompany.myapp.repository.ParameterRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Parameter}.
 */
@Service
@Transactional
public class ParameterService {

    private final Logger log = LoggerFactory.getLogger(ParameterService.class);

    private final ParameterRepository parameterRepository;

    public ParameterService(ParameterRepository parameterRepository) {
        this.parameterRepository = parameterRepository;
    }

    /**
     * Save a parameter.
     *
     * @param parameter the entity to save.
     * @return the persisted entity.
     */
    public Parameter save(Parameter parameter) {
        log.debug("Request to save Parameter : {}", parameter);
        return parameterRepository.save(parameter);
    }

    /**
     * Partially update a parameter.
     *
     * @param parameter the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Parameter> partialUpdate(Parameter parameter) {
        log.debug("Request to partially update Parameter : {}", parameter);

        return parameterRepository
            .findById(parameter.getId())
            .map(existingParameter -> {
                if (parameter.getLabel() != null) {
                    existingParameter.setLabel(parameter.getLabel());
                }
                if (parameter.getActivated() != null) {
                    existingParameter.setActivated(parameter.getActivated());
                }
                if (parameter.getLib2() != null) {
                    existingParameter.setLib2(parameter.getLib2());
                }
                if (parameter.getLib3() != null) {
                    existingParameter.setLib3(parameter.getLib3());
                }
                if (parameter.getRefExterne() != null) {
                    existingParameter.setRefExterne(parameter.getRefExterne());
                }
                if (parameter.getVal1() != null) {
                    existingParameter.setVal1(parameter.getVal1());
                }
                if (parameter.getVal2() != null) {
                    existingParameter.setVal2(parameter.getVal2());
                }
                if (parameter.getVal3() != null) {
                    existingParameter.setVal3(parameter.getVal3());
                }
                if (parameter.getOrdre() != null) {
                    existingParameter.setOrdre(parameter.getOrdre());
                }

                return existingParameter;
            })
            .map(parameterRepository::save);
    }

    /**
     * Get all the parameters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Parameter> findAll(Pageable pageable) {
        log.debug("Request to get all Parameters");
        return parameterRepository.findAll(pageable);
    }

    /**
     * Get one parameter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Parameter> findOne(Long id) {
        log.debug("Request to get Parameter : {}", id);
        return parameterRepository.findById(id);
    }

    /**
     * Delete the parameter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Parameter : {}", id);
        parameterRepository.deleteById(id);
    }
}
