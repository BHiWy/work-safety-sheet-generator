package services;


import jakarta.persistence.EntityManager;
import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.entities.Group;
import org.generator.entities.Student;
import org.generator.mapper.GroupMapper;
import org.generator.repository.GroupRepository;
import org.generator.services.GroupService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock //Creates false dependency
    private EntityManager entityManager;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks //Creates the class to be tested
    private GroupService groupService;

    @Test
    void testSaveValidGroupCode() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("1305A");

        Group groupEntity = new Group();
        when(groupMapper.toEntity(groupDTO)).thenReturn(groupEntity);

        groupService.save(groupDTO);

        verify(entityManager, times(1)).persist(groupEntity);
    }


    @Test
    void testSaveInvalidGroupCodeTooHigh() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("1307A");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            groupService.save(groupDTO);
        });

        Assertions.assertEquals("Codul grupei este invalid: 1307A", exception.getMessage());
    }

    @Test
    void testSaveInvalidGroupCodeLetterInvalid() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("1305C");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            groupService.save(groupDTO);
        });

        Assertions.assertEquals("Codul grupei este invalid: 1305C", exception.getMessage());
    }

    @Test
    void testSaveNullGroupCode() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode(null);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            groupService.save(groupDTO);
        });

        Assertions.assertEquals("Codul grupei este invalid: null", exception.getMessage());
    }


    @Test
    void testExtractYearValid() {
        String groupCode = "1304A";  //valid groupCode exemple

        int year = groupService.extractYear(groupCode);

        Assertions.assertEquals(3, year);
    }

    @Test
    void testFindStudentsByGroupCode() {
        String groupCode = "1305A";

        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("Ana");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Ion");

        List<Student> studentList = Arrays.asList(student1, student2);
        when(groupRepository.findDistinctStudentsByGroupCode(groupCode)).thenReturn(studentList);

        List<StudentDTO> result = groupService.findStudentsByGroupCode(groupCode);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Ana", result.get(0).getFirstName());
        Assertions.assertEquals("Ion", result.get(1).getFirstName());
    }

    @Test
    void testFindStudentsByGroupCode_EmptyList() {
        String groupCode = "1305A";
        when(groupRepository.findDistinctStudentsByGroupCode(groupCode)).thenReturn(Collections.emptyList());

        List<StudentDTO> result = groupService.findStudentsByGroupCode(groupCode);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty(), "Lista de studenți ar trebui să fie goală");
    }

    @Test
    void testFindStudentsByGroupCode_NullGroupCode_ThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> groupService.findStudentsByGroupCode(null));
    }


    @Test
    void testGetAllGroups() {
        Group group1 = new Group();
        group1.setCode("1305A");

        Group group2 = new Group();
        group2.setCode("1306B");

        Student s1 = new Student();
        s1.setFirstName("Ion");
        s1.setLastName("Popescu");

        group1.setStudents(List.of(s1));
        group2.setStudents(List.of());

        List<Group> groupList = Arrays.asList(group1, group2);
        when(groupRepository.findAll()).thenReturn(groupList);

        List<GroupDTO> result = groupService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("1305A", result.get(0).getCode());
        Assertions.assertEquals("1306B", result.get(1).getCode());
    }

    @Test
    void testGetAll_WhenNoGroupsExist_ReturnsEmptyList() {
        when(groupRepository.findAll()).thenReturn(Collections.emptyList());

        List<GroupDTO> result = groupService.getAll();

        Assertions.assertNotNull(result, "Lista returnată nu trebuie să fie null");
        Assertions.assertTrue(result.isEmpty(), "Lista trebuie să fie goală dacă nu există grupe");
    }
}
