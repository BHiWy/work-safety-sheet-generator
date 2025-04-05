package org.generator.services;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.generator.dto.GroupDTO;
import org.generator.entities.Group;
import org.generator.mapper.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
@Slf4j
public class GroupService {
    private final EntityManager entityManager;
    private final GroupMapper groupMapper;

    @Autowired
    public GroupService(EntityManager entityManager, GroupMapper groupMapper) {
        this.entityManager = entityManager;
        this.groupMapper = groupMapper;
    }

    @Transactional
    public void save(GroupDTO group) {
        Group groupEntity = groupMapper.toEntity(group);
        entityManager.persist(groupEntity);
    }
}
