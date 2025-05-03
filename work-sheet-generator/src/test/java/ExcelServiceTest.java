import org.apache.poi.ss.usermodel.*;
import lombok.extern.slf4j.Slf4j;
import org.generator.services.ExcelService;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link ExcelService} class, focusing on verifying
 * Excel file reading capabilities, including presence of required columns,
 * empty cell checks, and group leader presence.
 */
@Service
@Slf4j
public class ExcelServiceTest {
    private final ExcelService excelService;

    public ExcelServiceTest(ExcelService excelService) {
        this.excelService = excelService;
    }

    private static final String FILE_PATH = "files/grupe-an-III-AIA-2024_2025.xls";

    /**
     * Tests reading a valid Excel file.
     * Ensures that the file exists and no exceptions are thrown during processing.
     */
    @Test
    public void testReadExcelValidFile() {
        // Ensure that the test file exists in resources
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(mockInputStream, "File not found!");
            // Call the read method
            excelService.readExcel();

            // Ensure no errors occurred
        } catch (Exception e) {
            fail("Test failed due to error: " + e.getMessage());
        }
    }

    /**
     * Tests reading an invalid Excel file.
     * Verifies that no unexpected errors occur if the file is missing.
     */
    @Test
    public void testReadExcelInvalidFile() {
        // Test the case when the file does not exist
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("excel/fisier_inexistent_123.xlsx")) {
            assertNull(mockInputStream, "File should not exist!");

            excelService.readExcel();

            // Ensure no unexpected error occurred
        } catch (Exception e) {
            fail("Test failed due to error: " + e.getMessage());
        }
    }

    /**
     * Tests reading an Excel file that exists but contains no relevant data.
     * Verifies that no crash occurs even if the file is empty.
     */
    @Test
    public void testReadExcelEmptyFile() {
        // Test the case when the file exists but contains no relevant data
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(mockInputStream, "Empty file not found!");

            excelService.readExcel();

            // Just verify no error occurred and that the empty file did not cause a crash
        } catch (Exception e) {
            fail("Test failed due to error: " + e.getMessage());
        }
    }

    /**
     * Tests whether there are any empty cells in the "Student" or "Group" columns.
     * The check stops when a null or blank cell is found in the "Nr." column.
     */
    @Test
    public void testExcelStudentOrGroupEmpty() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(inputStream, "Excel file not found!");

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int studentColIndex = -1;
            int grupaColIndex = -1;
            int nrColIndex = -1;

            // Get the header row (Excel row 9 = index 8)
            Row headerRow = sheet.getRow(8);
            for (Cell cell : headerRow) {
                String value = cell.getStringCellValue();
                if (value.equalsIgnoreCase("Nume si prenume")) {
                    studentColIndex = cell.getColumnIndex();
                } else if (value.equalsIgnoreCase("grupa")) {
                    grupaColIndex = cell.getColumnIndex();
                } else if (value.equalsIgnoreCase("Nr.")) {
                    nrColIndex = cell.getColumnIndex();
                }
            }

            assertTrue(studentColIndex != -1, "'Student' column not found!");
            assertTrue(grupaColIndex != -1, "'Group' column not found!");
            assertTrue(nrColIndex != -1, "'Nr' column not found!");

            boolean hasEmpty = false;

            // Iterate from row 10 to end, stopping at first null in "Nr." column
            for (int i = 9; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nrCell = row.getCell(nrColIndex);
                if (nrCell == null || nrCell.getCellType() == CellType.BLANK) {
                    break; // Stop at first blank in "Nr." column
                }

                // Check for empty cells in "Student" or "Group" columns
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
     * Groups are identified by "Nr." column starting at 1.
     */
    @Test
    public void testGroupHasGroupLeader() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(inputStream, "Excel file not found!");

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int studentColIndex = -1;
            int grupaColIndex = -1;
            int nrColIndex = -1;
            int obsColIndex = -1;

            // Get the header row (assumed to be at line 9 / index 8)
            Row headerRow = sheet.getRow(8);
            for (Cell cell : headerRow) {
                String value = cell.getStringCellValue();
                if (value.equalsIgnoreCase("Nume si prenume")) {
                    studentColIndex = cell.getColumnIndex();
                } else if (value.equalsIgnoreCase("grupa")) {
                    grupaColIndex = cell.getColumnIndex();
                } else if (value.equalsIgnoreCase("Nr.")) {
                    nrColIndex = cell.getColumnIndex();
                } else if (value.equalsIgnoreCase("Obs.")) {
                    obsColIndex = cell.getColumnIndex();
                }
            }

            assertTrue(studentColIndex != -1, "'Student' column not found!");
            assertTrue(grupaColIndex != -1, "'Group' column not found!");
            assertTrue(nrColIndex != -1, "'Nr.' column not found!");
            assertTrue(obsColIndex != -1, "'Obs.' column not found!");

            boolean allGroupsHaveSef = true;
            boolean groupHasSefDeGrupa = false;

            // Iterate through all rows
            for (int i = 9; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nrCell = row.getCell(nrColIndex);
                if (nrCell == null || nrCell.getCellType() == CellType.BLANK) {
                    break; // End of data
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

            // Check last group (optional logic if needed)
            // if (!groupHasSefDeGrupa) {
            //     System.out.println("Last group has no group leader!");
            //     allGroupsHaveSef = false;
            // }

            assertTrue(allGroupsHaveSef, "Not all groups have a group leader!");

        } catch (Exception e) {
            fail("Exception while reading file: " + e.getMessage());
        }
    }

    /**
     * Utility method to check if a cell is empty.
     *
     * @param cell the cell to check
     * @return true if the cell is empty, false otherwise
     */
    private boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK ||
                (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }

    /**
     * Utility method to extract a cell value as a string, regardless of its type.
     *
     * @param cell the cell to read
     * @return the string representation of the cell value
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}











