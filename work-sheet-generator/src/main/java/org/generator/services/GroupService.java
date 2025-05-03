package org.generator.services;

import jakarta.transaction.Transactional;
import org.generator.dto.GroupDTO;
import org.generator.entities.Group;
import org.generator.mapper.GroupMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for managing {@link Group} entities.
 * Provides functionalities for saving group information and extracting the year of study from a group code.
 */
@Service
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    private final GroupMapper groupMapper;

    public GroupService(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    /**
     * Saves a {@link GroupDTO} to the database.
     * This operation is transactional. It maps the provided {@link GroupDTO}
     * to its corresponding {@link Group} entity and persists it using the entity manager.
     * @param groupDTO the {@link GroupDTO} object to be saved.
     */
    @Transactional
    public void save(GroupDTO groupDTO) {
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
}