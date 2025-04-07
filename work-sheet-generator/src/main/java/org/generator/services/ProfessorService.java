package org.generator.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.generator.entities.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Slf4j
public class ProfessorService {
    private final EntityManager entityManager;

    @Autowired
    public ProfessorService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(Professor professor){
        entityManager.persist(professor);
    }
}
