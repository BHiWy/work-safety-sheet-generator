package org.generator.services;

import jakarta.transaction.Transactional;
import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.entities.Group;
import org.generator.entities.Student;
import org.generator.mapper.GroupMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.generator.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Service class responsible for managing {@link Group} entities.
 * Provides functionalities for saving group information and extracting the year of study from a group code.
 */
@Service
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;

    /**
     * Constructor for the {@code GroupService} which injects the {@link EntityManager}, {@link GroupMapper} and {@link GroupRepository}.
     * @param entityManager the {@code EntityManager} instance for database interactions.
     * @param groupMapper the mapper for converting between {@link GroupDTO} and {@link Group} entities.
     * @param groupRepository the repository for {@link Group} entities.
     */
    public GroupService(EntityManager entityManager, GroupMapper groupMapper, GroupRepository groupRepository) {
        this.entityManager = entityManager;
        this.groupMapper = groupMapper;
        this.groupRepository = groupRepository;
    }

    /**
     * Saves a {@link GroupDTO} to the database.
     * This operation is transactional. It maps the provided {@link GroupDTO}
     * to its corresponding {@link Group} entity and persists it using the entity manager.
     * @param groupDTO the {@link GroupDTO} object to be saved.
     * @throws IllegalArgumentException if the group code in the {@code groupDTO} is null or does not match the pattern "\\d{4}[A-Za-z]".
     */
    @Transactional
    public void save(GroupDTO groupDTO) {
        if (groupDTO.getCode() == null || !groupDTO.getCode().matches("130[1-6][AB]")) {
            throw new IllegalArgumentException("Codul grupei este invalid: " + groupDTO.getCode());
        }
        Group groupEntity = groupMapper.toEntity(groupDTO);
        entityManager.persist(groupEntity);
    }

    /**
     * Extracts the year of study from the group code.
     * Example: for "1304A", returns 3.
     * @param groupCode the group code (e.g., "1304A")
     * @return the extracted year of study
     * @throws IllegalArgumentException if the group code is invalid.
     */
    public int extractYear(String groupCode) {
        if (groupCode == null || groupCode.length() < 2) {
            throw new IllegalArgumentException("Codul grupei este invalid: " + groupCode);
        }

        char secondChar = groupCode.charAt(1);

        if (!Character.isDigit(secondChar)) {
            throw new IllegalArgumentException("A doua poziție din codul grupei nu este o cifră: " + groupCode);
        }

        return Character.getNumericValue(secondChar);
    }

    /**
     * Retrieves a distinct list of {@link StudentDTO} objects belonging to a group
     * with the specified group code.
     * It queries the database using the {@code groupRepository} to find the students
     * associated with the given group code and then maps each {@link Student} entity
     * to its corresponding {@link StudentDTO}.
     *
     * @param groupCode the unique code of the group.
     * @return a {@link List} of distinct {@link StudentDTO} objects belonging to the specified group.
     */
    public List<StudentDTO> findStudentsByGroupCode(String groupCode) {
        if (groupCode == null) {
            throw new IllegalArgumentException("Codul grupei nu poate fi null");
        }
        return groupRepository.findDistinctStudentsByGroupCode(groupCode)
                .stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all groups and returns them as a list of {@link GroupDTO}s.
     * @return {List<{@link GroupDTO}>} A list containing all groups mapped to their DTO representation.
     */
    public List<GroupDTO> getAll(){
        return groupRepository.findAll().stream()
                .map(GroupDTO::new)
                .collect(Collectors.toList());
    }
}