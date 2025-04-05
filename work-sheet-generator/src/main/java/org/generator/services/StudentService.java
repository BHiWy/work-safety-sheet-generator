package org.generator.services;

import org.generator.dto.StudentDTO;
import org.generator.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.generator.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Slf4j
public class StudentService {
    private final EntityManager entityManager;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentService(EntityManager entityManager, StudentMapper studentMapper) {
        this.entityManager = entityManager;
        this.studentMapper = studentMapper;
    }

    @Transactional
    public void save(StudentDTO student){
        Student stud = studentMapper.toEntity(student);
        entityManager.persist(stud);
    }
}
