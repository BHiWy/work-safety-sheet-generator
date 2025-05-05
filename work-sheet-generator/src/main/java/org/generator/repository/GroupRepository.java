package org.generator.repository;

import org.generator.entities.Group;
import org.generator.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository for {@link Group} entities, providing CRUD operations and custom queries.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {
    /**
     * Finds distinct students by a given group code.
     * @param {String} code The code of the group.
     * @return {List<{@link Student}>} A list of unique students in the specified group.
     */
    @Query("SELECT DISTINCT s FROM Group g JOIN g.students s WHERE g.code = :code")
    List<Student> findDistinctStudentsByGroupCode(@Param("code") String code);
}
