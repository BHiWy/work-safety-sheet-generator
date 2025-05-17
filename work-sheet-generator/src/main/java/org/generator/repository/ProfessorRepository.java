package org.generator.repository;

import org.generator.entities.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for {@link Professor} entities, providing CRUD operations and custom queries.
 */
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    /**
     * Retrieves unique professors by their rank.
     *
     * @param rank The rank of the professors (as a string).
     * @return A unique {@link Professor} list of professors with the given rank.
     */
    @Query("SELECT DISTINCT pf FROM Professor pf WHERE pf.rank = :rank")
    List<Professor> findByRank(String rank);

    /**
     * Finds course names by a professor's ID.
     * @param id The professor's ID.
     * @return A list of course names.
     */
    @Query("SELECT p.courses FROM Professor p WHERE p.id = :id")
    List<String> findCoursesByProfessorId(long id);
}
