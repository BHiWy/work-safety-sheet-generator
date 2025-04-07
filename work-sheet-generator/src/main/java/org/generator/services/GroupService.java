package org.generator.services;

import jakarta.transaction.Transactional;
import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.entities.Group;
import org.generator.entities.Student;
import org.generator.mapper.GroupMapper;
import org.generator.mapper.StudentMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;


@Service
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;
    private final StudentService studentService;

    public GroupService(GroupMapper groupMapper, StudentMapper studentMapper, StudentService studentService) {
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
        this.studentService = studentService;
    }

    @Transactional
    public void save(GroupDTO groupDTO) {
        Group groupEntity = groupMapper.toEntity(groupDTO);

        if (groupDTO.getStudents() != null && !groupDTO.getStudents().isEmpty()) {
            for (StudentDTO studentDTO : groupDTO.getStudents()) {
                Student student = studentMapper.toEntity(studentDTO);
                studentService.save(student); // Salveaza fiecare student individual
                groupEntity.getStudents().add(student); // Adauga studentul in groupa
            }
        }

        if (groupDTO.getGroupLeader() != null) {
            StudentDTO groupLeaderDTO = groupDTO.getGroupLeader();
            Student groupLeaderEntity = studentMapper.toEntity(groupLeaderDTO);
            studentService.save(groupLeaderEntity); // Salveaza liderul de grup individual
            groupEntity.setGroupLeader(groupLeaderEntity);
        }

        entityManager.persist(groupEntity);
    }
}