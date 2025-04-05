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

import java.util.ArrayList;

@Service
public class GroupService {

    @PersistenceContext
    private EntityManager entityManager;

    private final GroupMapper groupMapper;
    private final StudentMapper studentMapper;
    private final StudentService studentService; // Injectează StudentService

    public GroupService(GroupMapper groupMapper, StudentMapper studentMapper, StudentService studentService) {
        this.groupMapper = groupMapper;
        this.studentMapper = studentMapper;
        this.studentService = studentService;
    }

    @Transactional
    public void save(GroupDTO groupDTO) {
        Group groupEntity = groupMapper.toEntity(groupDTO);
        ArrayList<Student> studentsToPersist = new ArrayList<>();

        if (groupDTO.getStudents() != null && !groupDTO.getStudents().isEmpty()) {
            for (StudentDTO studentDTO : groupDTO.getStudents()) {
                Student student = studentMapper.toEntity(studentDTO);
                studentService.save(student); // Salvează fiecare student individual
                studentsToPersist.add(student);
            }
            groupEntity.setStudents(studentsToPersist);
        }

        if (groupDTO.getGroupLeader() != null) {
            StudentDTO groupLeaderDTO = groupDTO.getGroupLeader();
            Student groupLeaderEntity = studentMapper.toEntity(groupLeaderDTO);
            studentService.save(groupLeaderEntity); // Salvează liderul de grup individual
            groupEntity.setGroupLeader(groupLeaderEntity);
        }

        entityManager.persist(groupEntity);
    }
}