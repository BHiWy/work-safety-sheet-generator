package org.generator.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.generator.dto.ProfessorDTO;
import org.generator.entities.Professor;
import org.generator.mapper.ProfessorMapper;
import org.generator.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing {@link Professor} entities.
 * Provides functionalities for saving professor information.
 */
@Transactional
@Service
@Slf4j
public class ProfessorService {
    private final EntityManager entityManager;
    private final ProfessorRepository professorRepository;
    private final ProfessorMapper professorMapper;

    /**
     * Constructs a {@code ProfessorService} which injects the {@link EntityManager} and {@link ProfessorRepository}.
     *
     * @param entityManager the JPA entity manager used for database operations.
     * @param professorRepository the repository for {@link Professor} entities.
     * @param professorMapper the mapper for converting between {@link ProfessorDTO} and {@link Professor} entities.
     */
    @Autowired
    public ProfessorService(
            EntityManager entityManager,
            ProfessorRepository professorRepository,
            ProfessorMapper professorMapper) {
        this.entityManager = entityManager;
        this.professorRepository = professorRepository;
        this.professorMapper = professorMapper;
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

    /**
     * Finds all professors with the given rank.
     *
     * @param rank The rank to search for (case-sensitive).
     * @return A {@link List} of {@link ProfessorDTO} for professors with the specified rank.
     */
    public List<ProfessorDTO> findAllByRank(String rank){
        return this.professorRepository.findByRank(rank).stream().map(professorMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of course names taught by the professor with the given ID.
     * This method interacts with the {@link ProfessorRepository} to fetch the course data.
     * @param id The unique identifier of the professor.
     * @return A {@link List} of {@link String}, where each string represents the name of a course taught by the professor.
     */
    public List<String> findCoursesByProfessor(long id){
        return this.professorRepository.findCoursesByProfessorId(id);
    }
}
