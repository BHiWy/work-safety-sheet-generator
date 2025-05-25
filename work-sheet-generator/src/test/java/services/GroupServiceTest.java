package services;


import jakarta.persistence.EntityManager;
import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.entities.Group;
import org.generator.entities.Student;
import org.generator.mapper.GroupMapper;
import org.generator.mapper.StudentMapper;
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

/**
 * Unit tests for the {@link GroupService} class.
 * <p>
 * Verifies group code validation, saving logic, student retrieval by group code,
 * and fetching all groups from the repository.
 */
@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock //Creates false dependency
    private EntityManager entityManager;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks //Creates the class to be tested
    private GroupService groupService;

    /**
     * Tests saving a group with a valid code.
     *
     * <p>Verifies that when a {@link GroupDTO} with a valid code is saved,
     * the corresponding {@link Group} entity is persisted.
     *
     * @see GroupService#save(GroupDTO)
     */
    @Test
    void testSaveValidGroupCode() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("1305A");

        Group groupEntity = new Group();
        when(groupMapper.toEntity(groupDTO)).thenReturn(groupEntity);

        groupService.save(groupDTO);

        verify(entityManager, times(1)).persist(groupEntity);
    }

    /**
     * Tests saving a group with an invalid code (too high).
     *
     * <p>Verifies that attempting to save a {@link GroupDTO} with a group code exceeding the allowed limit
     * results in an {@link IllegalArgumentException} with the expected message.
     *
     * @throws IllegalArgumentException if the group code is invalid.
     * @see GroupService#save(GroupDTO)
     */
    @Test
    void testSaveInvalidGroupCodeTooHigh() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("1307A");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> groupService.save(groupDTO));

        Assertions.assertEquals("Codul grupei este invalid: 1307A", exception.getMessage());
    }

    /**
     * Tests saving a group with an invalid code (invalid letter).
     *
     * <p>Verifies that attempting to save a {@link GroupDTO} with a group code containing an invalid letter
     * results in an {@link IllegalArgumentException} with the expected message.
     *
     * @throws IllegalArgumentException if the group code contains an invalid letter.
     * @see GroupService#save(GroupDTO)
     */
    @Test
    void testSaveInvalidGroupCodeLetterInvalid() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("1305C");

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> groupService.save(groupDTO));

        Assertions.assertEquals("Codul grupei este invalid: 1305C", exception.getMessage());
    }

    /**
     * Tests saving a group with a null code.
     *
     * <p>Verifies that attempting to save a {@link GroupDTO} with a null group code
     * results in an {@link IllegalArgumentException} with the expected message.
     *
     * @throws IllegalArgumentException if the group code is null.
     * @see GroupService#save(GroupDTO)
     */
    @Test
    void testSaveNullGroupCode() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode(null);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> groupService.save(groupDTO));

        Assertions.assertEquals("Codul grupei este invalid: null", exception.getMessage());
    }


    /**
     * Tests extracting the year from a valid group code.
     *
     * <p>Verifies that for a valid group code, the {@code extractYear} method
     * correctly extracts and returns the year.
     *
     * @see GroupService#extractYear(String)
     */
    @Test
    void testExtractYearValid() {
        String groupCode = "1304A";  //valid groupCode exemple

        int year = groupService.extractYear(groupCode);

        Assertions.assertEquals(3, year);
    }

    /**
     * Tests finding students by a given group code.
     *
     * <p>Verifies that when a group code is provided, the {@code findStudentsByGroupCode} method
     * correctly retrieves and returns a list of {@link StudentDTO} associated with that group.
     * It mocks the {@link GroupRepository} to return a predefined list of {@link Student} entities.
     *
     * @see GroupService#findStudentsByGroupCode(String)
     * @see GroupRepository#findDistinctStudentsByGroupCode(String)
     */
    @Test
    void testFindStudentsByGroupCode() {
        String groupCode = "1305A";

        // Entități Student
        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstName("Ana");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstName("Ion");

        List<Student> studentList = Arrays.asList(student1, student2);
        when(groupRepository.findDistinctStudentsByGroupCode(groupCode)).thenReturn(studentList);

        StudentDTO dto1 = new StudentDTO();
        dto1.setFirstName("Ana");

        StudentDTO dto2 = new StudentDTO();
        dto2.setFirstName("Ion");

        when(studentMapper.toDTO(student1)).thenReturn(dto1);
        when(studentMapper.toDTO(student2)).thenReturn(dto2);

        List<StudentDTO> result = groupService.findStudentsByGroupCode(groupCode);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Ana", result.get(0).getFirstName());
        Assertions.assertEquals("Ion", result.get(1).getFirstName());
    }

    /**
     * Tests finding students by a group code when no students are associated with it.
     *
     * <p>Verifies that when a group code is provided and no students are found in the repository,
     * the {@code findStudentsByGroupCode} method returns an empty list of {@link StudentDTO}.
     * It mocks the {@link GroupRepository} to return an empty list.
     *
     * @see GroupService#findStudentsByGroupCode(String)
     * @see GroupRepository#findDistinctStudentsByGroupCode(String)
     */
    @Test
    void testFindStudentsByGroupCode_EmptyList() {
        String groupCode = "1305A";
        when(groupRepository.findDistinctStudentsByGroupCode(groupCode)).thenReturn(Collections.emptyList());

        List<StudentDTO> result = groupService.findStudentsByGroupCode(groupCode);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty(), "Lista de studenți ar trebui să fie goală");
    }

    /**
     * Tests finding students by a null group code.
     *
     * <p>Verifies that calling the {@code findStudentsByGroupCode} method with a null group code
     * results in an {@link IllegalArgumentException}.
     *
     * @throws IllegalArgumentException if the group code is null.
     * @see GroupService#findStudentsByGroupCode(String)
     */
    @Test
    void testFindStudentsByGroupCode_NullGroupCode_ThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> groupService.findStudentsByGroupCode(null));
    }

    /**
     * Tests retrieving all groups.
     *
     * <p>Verifies that the {@code getAll} method correctly retrieves all {@link Group} entities
     * from the repository and maps them to a list of {@link GroupDTO}. It mocks the {@link GroupRepository}
     * to return a predefined list of {@link Group} entities.
     *
     * @see GroupService#getAll()
     * @see GroupRepository#findAll()
     */
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

        GroupDTO dto1 = new GroupDTO();
        dto1.setCode("1305A");

        GroupDTO dto2 = new GroupDTO();
        dto2.setCode("1306B");

        when(groupMapper.toDTO(group1)).thenReturn(dto1);
        when(groupMapper.toDTO(group2)).thenReturn(dto2);

        List<GroupDTO> result = groupService.getAll();

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("1305A", result.get(0).getCode());
        Assertions.assertEquals("1306B", result.get(1).getCode());
    }

    /**
     * Tests retrieving all groups when no groups exist in the database.
     *
     * <p>Verifies that the {@code getAll} method returns an empty list of {@link GroupDTO}
     * when the {@link GroupRepository} returns an empty list.
     *
     * @see GroupService#getAll()
     * @see GroupRepository#findAll()
     */
    @Test
    void testGetAll_WhenNoGroupsExist_ReturnsEmptyList() {
        when(groupRepository.findAll()).thenReturn(Collections.emptyList());

        List<GroupDTO> result = groupService.getAll();

        Assertions.assertNotNull(result, "Lista returnată nu trebuie să fie null");
        Assertions.assertTrue(result.isEmpty(), "Lista trebuie să fie goală dacă nu există grupe");
    }
}
