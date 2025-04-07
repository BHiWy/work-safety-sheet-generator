package org.generator.services;

import org.generator.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Slf4j
public class StudentService {
    private final EntityManager entityManager;

    @Autowired
    public StudentService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(Student student){
        entityManager.persist(student);
    }
}
