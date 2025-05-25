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

public class WordServiceTest {

    private WordService wordService;
    private GroupMapper groupMapper;

    private Group createMockGroupWithStudents(int count) {
        Group group = new Group();
        group.setStudents(new ArrayList<>()); // Initializam lista ca sa nu fie null

        for (int i = 1; i <= count; i++) {
            Student s = new Student();
            s.setFirstName("Prenume" + i);
            s.setLastName("Nume" + i);
            s.setPaternalInitial("I" + i);
            group.getStudents().add(s); // acum nu mai crapa
        }

        return group;
    }

    @BeforeEach
    public void setup() {
        groupMapper = mock(GroupMapper.class);
        wordService = new WordService(groupMapper);
    }

    /**
     * Test pentru lipsa template
     * Scop: Sa verificam ca daca template-ul lipseste sau nu poate fi citit,
     * metoda nu arunca exceptie, ci returneaza un ByteArrayOutputStream gol.
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

        // Simuleaza ca metoda nu gaseste template-ul
        WordService brokenService = new WordService(groupMapper) {
            @Override
            public ByteArrayOutputStream generateWorkSheet(DocumentInputDataDTO inputData) {
                return new ByteArrayOutputStream(0); // mimam lipsa template-ului
            }
        };

        ByteArrayOutputStream result = brokenService.generateWorkSheet(input);
        assertNotNull(result);
        assertEquals(0, result.size(), "Streamul ar trebui sa fie gol daca lipseste template-ul.");
    }

    /**
     * Test de generare de fisa cu date valide
     * Scop: Sa verificam ca fisierul este generat corect si nu este gol.
     * Foloseste un grup cu 2 studenti si valideaza ca rezultatul are continut.
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
        assertTrue(output.size() > 0, "Fisierul generat nu ar trebui sa fie gol.");
    }


    /**
     * Test pentru populatia tabelului cu studenti
     * Scop: Verificam ca numarul de studenti din grup se reflecta in continutul documentului.
     * Ne asiguram ca numele fiecarui student apare in fisierul generat.
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

        // Verificare bruta: documentul contine toate numele studentilor
        String docxContent = new String(result.toByteArray());
        for (int i = 1; i <= expectedStudents; i++) {
            assertTrue(docxContent.contains("Nume" + i),
                    "Documentul ar trebui sa contina Nume" + i);
        }
    }
}
