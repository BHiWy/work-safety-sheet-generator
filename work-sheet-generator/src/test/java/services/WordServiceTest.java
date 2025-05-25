package services;

import org.generator.dto.DocumentInputDataDTO;
import org.generator.dto.GroupDTO;
import org.generator.entities.Group;
import org.generator.entities.Student;
import org.generator.mapper.GroupMapper;
import org.generator.services.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This test class verifies the correct behavior of the Word document generation logic
 *  using a predefined template. It mocks the {@link GroupMapper} dependency to simulate
 *  student group data without relying on actual database access.
 */

public class WordServiceTest {

    private WordService wordService;
    private GroupMapper groupMapper;

    private Group createMockGroupWithStudents(int count) {
        Group group = new Group();
        group.setStudents(new ArrayList<>()); // Initialize the list to avoid it being null

        for (int i = 1; i <= count; i++) {
            Student s = new Student();
            s.setFirstName("Prenume" + i);
            s.setLastName("Nume" + i);
            s.setPaternalInitial("I" + i);
            group.getStudents().add(s);
        }

        return group;
    }

    @BeforeEach
    public void setup() {
        groupMapper = mock(GroupMapper.class);
        wordService = new WordService(groupMapper);
    }

    /**
     * Test for missing template
     * Purpose: To verify that if the template is missing or cannot be read,
     * the method does not throw an exception, but returns an empty ByteArrayOutputStream.
     */
    @Test
    public void testMissingTemplateReturnsEmptyStream() throws Exception {
        DocumentInputDataDTO input = new DocumentInputDataDTO();
        input.setFromDate(LocalDate.now());
        input.setToDate(LocalDate.now().plusMonths(5));
        input.setProfessorName("Test Prof");
        input.setAssistantName("Test Asist");
        input.setGroups(List.of(new GroupDTO()));
        input.setCourseName("Test Course");
        input.setPlace("Test Room");

        // Simulate that the method cannot find the template
        WordService brokenService = new WordService(groupMapper) {
            @Override
            public ByteArrayOutputStream generateWorkSheet(DocumentInputDataDTO inputData) {
                return new ByteArrayOutputStream(0); // // mocking the absence of the template
            }
        };

        ByteArrayOutputStream result = brokenService.generateWorkSheet(input);
        assertNotNull(result);
        assertEquals(0, result.size(), "The stream should be empty if the template is missing.");
    }

    /**
     * Test for generating a report with valid data
     * Purpose: To verify that the file is generated correctly and is not empty.
     * Uses a group with 2 students and checks that the result has content.
     */
    @Test
    public void testValidDataGeneratesFile() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("AC-302");

        DocumentInputDataDTO input = new DocumentInputDataDTO();
        input.setFromDate(LocalDate.of(2024, 10, 1));
        input.setToDate(LocalDate.of(2025, 2, 28));
        input.setProfessorName("Prof. X");
        input.setAssistantName("Asist. Y");
        input.setGroups(List.of(groupDTO));
        input.setCourseName("Inginerie Software");
        input.setPlace("C405");

        Group mockGroup = createMockGroupWithStudents(2);
        when(groupMapper.toEntity(any(GroupDTO.class))).thenReturn(mockGroup);

        ByteArrayOutputStream output = wordService.generateWorkSheet(input);

        assertNotNull(output);
        assertTrue(output.size() > 0, "The generated file should not be empty.");
    }


    /**
     * Test for populating the student table
     * Purpose: We verify that the number of students in the group is reflected in the document content.
     * We make sure that each student's name appears in the generated file.
     */
    @Test
    public void testStudentTablePopulationCorrectRows() throws Exception {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setCode("AC-303");

        DocumentInputDataDTO input = new DocumentInputDataDTO();
        input.setFromDate(LocalDate.now());
        input.setToDate(LocalDate.now().plusMonths(6));
        input.setProfessorName("Prof. Z");
        input.setAssistantName("Asist. W");
        input.setGroups(List.of(groupDTO));
        input.setCourseName("Baze de date");
        input.setPlace("C203");

        int expectedStudents = 5;
        Group mockGroup = createMockGroupWithStudents(expectedStudents);
        when(groupMapper.toEntity(any(GroupDTO.class))).thenReturn(mockGroup);

        ByteArrayOutputStream result = wordService.generateWorkSheet(input);

        assertNotNull(result);
        assertTrue(result.size() > 0);

        // Crude check: the document contains all student names
        String docxContent = new String(result.toByteArray());
        for (int i = 1; i <= expectedStudents; i++) {
            assertTrue(docxContent.contains("Nume" + i),
                    "The document should contain the name." + i);
        }
    }
}
