package org.generator.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.generator.entities.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for managing {@link Professor} entities.
 * Provides functionalities for saving professor information.
 */
@Transactional
@Service
@Slf4j
public class ProfessorService {
    private final EntityManager entityManager;

    /**
     * Constructs a {@code ProfessorService} which injects the {@link EntityManager}.
     *
     * @param entityManager the JPA entity manager used for database operations.
     */
    @Autowired
    public ProfessorService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persists a {@link Professor} entity into the database.
     * This operation is transactional, ensuring data consistency during the save operation.
     *
     * @param professor the {@link Professor} object to be saved.
     */
    @Transactional
    public void save(Professor professor){
        entityManager.persist(professor);
    }
}
