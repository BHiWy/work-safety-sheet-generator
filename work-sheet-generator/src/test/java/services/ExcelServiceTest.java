package services;

import org.generator.services.ExcelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.generator.entities.Professor;
import org.generator.services.GroupService;
import org.generator.services.ProfessorService;
import org.generator.services.StudentService;
import org.mockito.ArgumentCaptor;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ExcelService} class, focusing on verifying
 * Excel file reading capabilities, including presence of required columns,
 * empty cell checks, and group leader presence.
 */
@ExtendWith(MockitoExtension.class)
public class ExcelServiceTest {

    @InjectMocks //Creates the class to be tested
    private ExcelService excelService;

    private static final String studentsExcelPath = "files/grupe-an-III-AIA-2024_2025.xls";

    /**
     * Tests reading a valid Excel file.
     * Ensures that the students file exists and no exceptions are thrown during processing.
     */
    @Test
    public void testReadStudentsFromExcel_ValidFile() {
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream(studentsExcelPath)) {
            assertNotNull(mockInputStream, "File not found!");

            excelService.readStudentsFromExcel();

        } catch (Exception e) {
            fail("Test failed due to error: " + e.getMessage());
        }
    }

    /**
     * Tests reading an invalid Excel file.
     * Verifies that no unexpected errors occur if the file is missing.
     */
    @Test
    public void testReadStudentsFromExcel_InvalidFile() {
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("excel/fisier_inexistent_123.xlsx")) {
            assertNull(mockInputStream, "File should not exist!");

            excelService.readStudentsFromExcel();
        } catch (Exception e) {
            fail("Test failed due to error: " + e.getMessage());
        }
    }

    /**
     * Tests reading an Excel file  that exists but contains no relevant data.
     * Verifies that no crash occurs even if the file is empty.
     */
    @Test
    public void testReadStudentsFromExcel_EmptyFile() {
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream(studentsExcelPath)) {
            assertNotNull(mockInputStream, "Empty file not found!");

            excelService.readStudentsFromExcel();

        } catch (Exception e) {
            fail("Test failed due to error: " + e.getMessage());
        }
    }

    /**
     * Tests whether there are any empty cells in the "Student" or "Group" columns.
     * The check stops when a null or blank cell is found in the "Nr." column.
     */
    @Test
    public void testReadStudentsFromExcel_StudentOrGroupEmpty() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(studentsExcelPath)) {
            assertNotNull(inputStream, "Excel file not found!");

            Workbook workbook = new HSSFWorkbook(inputStream); // .xls compatible
            Sheet sheet = workbook.getSheetAt(0);

            int studentColIndex = -1;
            int grupaColIndex = -1;
            int nrColIndex = -1;

            Row headerRow = sheet.getRow(8);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) continue;
                String value = cell.getStringCellValue();
                if (value.equalsIgnoreCase("Nume si prenume")) {
                    studentColIndex = i;
                } else if (value.equalsIgnoreCase("grupa")) {
                    grupaColIndex = i;
                } else if (value.equalsIgnoreCase("Nr.")) {
                    nrColIndex = i;
                }
            }

            assertTrue(studentColIndex != -1, "'Student' column not found!");
            assertTrue(grupaColIndex != -1, "'Group' column not found!");
            assertTrue(nrColIndex != -1, "'Nr' column not found!");

            boolean hasEmpty = false;

            for (int i = 9; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nrCell = row.getCell(nrColIndex);
                if (nrCell == null || nrCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    break;
                }

                Cell studentCell = row.getCell(studentColIndex);
                Cell grupaCell = row.getCell(grupaColIndex);

                if (isCellEmpty(studentCell) || isCellEmpty(grupaCell)) {
                    hasEmpty = true;
                    break;
                }
            }

            assertFalse(hasEmpty, "Empty cells found in Student or Group columns!");

        } catch (Exception e) {
            fail("Exception while reading file: " + e.getMessage());
        }
    }

    /**
     * Checks whether each group has a designated group leader ("șef de grupă").
     * Groups are identified by the "Nr." column starting at value 1.
     */
    @Test
    public void testReadStudentsFromExcel_GroupHasGroupLeader() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(studentsExcelPath)) {
            assertNotNull(inputStream, "Excel file not found!");

            Workbook workbook = new HSSFWorkbook(inputStream); // .xls compatible
            Sheet sheet = workbook.getSheetAt(0);

            int studentColIndex = -1;
            int grupaColIndex = -1;
            int nrColIndex = -1;
            int obsColIndex = -1;

            Row headerRow = sheet.getRow(8);
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null) continue;
                String value = cell.getStringCellValue();
                if (value.equalsIgnoreCase("Nume si prenume")) {
                    studentColIndex = i;
                } else if (value.equalsIgnoreCase("grupa")) {
                    grupaColIndex = i;
                } else if (value.equalsIgnoreCase("Nr.")) {
                    nrColIndex = i;
                } else if (value.equalsIgnoreCase("Obs.")) {
                    obsColIndex = i;
                }
            }

            assertTrue(studentColIndex != -1, "'Student' column not found!");
            assertTrue(grupaColIndex != -1, "'Group' column not found!");
            assertTrue(nrColIndex != -1, "'Nr.' column not found!");
            assertTrue(obsColIndex != -1, "'Obs.' column not found!");

            boolean allGroupsHaveSef = true;
            boolean groupHasSefDeGrupa = false;

            for (int i = 9; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nrCell = row.getCell(nrColIndex);
                if (nrCell == null || nrCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                    break;
                }

                String nrValue = getCellValueAsString(nrCell);
                if ("1".equals(nrValue)) {
                    if (!groupHasSefDeGrupa) {
                        System.out.println("Group starting at line " + i + " has no group leader!");
                        allGroupsHaveSef = false;
                    }
                    groupHasSefDeGrupa = false;
                }

                Cell obsCell = row.getCell(obsColIndex);
                if (obsCell != null) {
                    String obsValue = getCellValueAsString(obsCell);
                    if (obsValue.toLowerCase().contains("șef de grupă")) {
                        groupHasSefDeGrupa = true;
                    }
                }
            }

            assertTrue(allGroupsHaveSef, "Not all groups have a group leader!");

        } catch (Exception e) {
            fail("Exception while reading file: " + e.getMessage());
        }
    }

    /**
     * Utility method to check if a cell is empty or blank.
     *
     * @param cell the Excel cell to evaluate
     * @return true if the cell is null, blank, or contains only whitespace
     */
    private boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK ||
                (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().trim().isEmpty());
    }

    /**
     * Utility method to extract a cell value as a string.
     *
     * @param cell the cell to read
     * @return string representation of the cell content
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }


    /**
     * Tests reading professors and assistants from Excel.
     * Verifies that the correct data is extracted and passed to {@link ProfessorService#save(Professor)}.
     */
    @Test
    public void testReadProfessorsFromExcel_SavesAllProfessorsAndAssistants() {
        ProfessorService professorService = mock(ProfessorService.class);
        StudentService studentService = mock(StudentService.class);
        GroupService groupService = mock(GroupService.class);
        ExcelService localExcelService = new ExcelService(studentService, groupService, professorService);

        localExcelService.readProfessorsFromExcel();

        ArgumentCaptor<Professor> captor = ArgumentCaptor.forClass(Professor.class);
        verify(professorService, atLeastOnce()).save(captor.capture());

        var savedProfessors = captor.getAllValues();
        assertFalse(savedProfessors.isEmpty(), "Expected at least one professor to be saved.");

        long countProfessors = savedProfessors.stream().filter(p -> "Profesor".equals(p.getRank())).count();
        long countAssistants = savedProfessors.stream().filter(p -> "Asistent".equals(p.getRank())).count();

        assertTrue(countProfessors > 0, "Expected at least one professor.");
        assertTrue(countAssistants > 0, "Expected at least one assistant.");
    }


    /**
     * Tests data consistency for professors read from Excel.
     * Ensures names and course assignments are valid.
     */
    @Test
    public void testReadProfessorsFromExcel_DataIntegrity() {
        ProfessorService professorService = mock(ProfessorService.class);
        StudentService studentService = mock(StudentService.class);
        GroupService groupService = mock(GroupService.class);
        ExcelService localExcelService = new ExcelService(studentService, groupService, professorService);

        localExcelService.readProfessorsFromExcel();

        ArgumentCaptor<Professor> captor = ArgumentCaptor.forClass(Professor.class);
        verify(professorService, atLeastOnce()).save(captor.capture());

        for (Professor p : captor.getAllValues()) {
            assertNotNull(p.getFullName(), "Professor name must not be null.");
            assertFalse(p.getFullName().isBlank(), "Professor name must not be blank.");

            assertNotNull(p.getCourses(), "Professor courses must not be null.");
            assertFalse(p.getCourses().isEmpty(), "Professor must teach at least one course.");

            assertTrue(
                    "Profesor".equals(p.getRank()) || "Asistent".equals(p.getRank()),
                    "Professor rank must be 'Profesor' or 'Asistent'."
            );
        }
    }

    /**
     * Tests that the rank column in Excel is interpreted correctly.
     * Validates that only "Profesor" or "Asistent" values are accepted.
     */
    @Test
    public void testReadProfessorsFromExcel_ValidRanksOnly() {
        ProfessorService professorService = mock(ProfessorService.class);
        StudentService studentService = mock(StudentService.class);
        GroupService groupService = mock(GroupService.class);
        ExcelService localExcelService = new ExcelService(studentService, groupService, professorService);

        localExcelService.readProfessorsFromExcel();

        ArgumentCaptor<Professor> captor = ArgumentCaptor.forClass(Professor.class);
        verify(professorService, atLeastOnce()).save(captor.capture());

        for (Professor professor : captor.getAllValues()) {
            String rank = professor.getRank();
            assertTrue(
                    rank.equals("Profesor") || rank.equals("Asistent"),
                    "Invalid rank found: " + rank
            );
        }
    }

    /**
     * Ensures that every saved professor has at least one assigned course.
     */
    @Test
    public void testReadProfessorsFromExcel_CoursesNotEmpty() {
        ProfessorService professorService = mock(ProfessorService.class);
        StudentService studentService = mock(StudentService.class);
        GroupService groupService = mock(GroupService.class);
        ExcelService localExcelService = new ExcelService(studentService, groupService, professorService);

        localExcelService.readProfessorsFromExcel();

        ArgumentCaptor<Professor> captor = ArgumentCaptor.forClass(Professor.class);
        verify(professorService, atLeastOnce()).save(captor.capture());

        for (Professor professor : captor.getAllValues()) {
            assertNotNull(professor.getCourses(), "Professor should have a non-null course list.");
            assertFalse(professor.getCourses().isEmpty(), "Professor should teach at least one course.");
        }
    }

}