

import org.apache.poi.ss.usermodel.*;
import org.generator.services.ExcelService;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class ExcelServiceTest {

    private static final String FILE_PATH = "files/grupe-an-III-AIA-2024_2025.xls";

    @Test
    public void testReadExcel_validFile() {
        // Verificam ca fisierul de test exista in resources
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(mockInputStream, "Fisierul nu a fost gasit!");

            // Cream instanta serviciului
            ExcelService excelService = new ExcelService();

            // Apelam metoda de citire
            excelService.readExcel();

            // Ne asiguram ca nu au aparut erori

        } catch (Exception e) {
            fail("Testul a esuat din cauza unei erori: " + e.getMessage());
        }
    }

    @Test
    public void testReadExcel_invalidFile() {
        // Testam cazul in care fișierul nu exista
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("excel/fisier_inexistent_123.xlsx")) {
            assertNull(mockInputStream, "Fisierul nu ar trebui sa existe!");

            ExcelService excelService = new ExcelService();
            excelService.readExcel();

            // Verificam ca nu a aparut nicio eroare neasteptata
        } catch (Exception e) {
            fail("Testul a esuat din cauza unei erori: " + e.getMessage());
        }
    }

    @Test
    public void testReadExcel_emptyFile() {
        // Testam cazul in care fisierul exista dar nu contine date relevante
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(mockInputStream, "Fisierul gol nu a fost gasit!");

            ExcelService excelService = new ExcelService();
            excelService.readExcel();

            // Aici, doar verificam ca nu a aparut nicio eroare, iar fisierul gol nu a cauzat un crash
        } catch (Exception e) {
            fail("Testul a esuat din cauza unei erori: " + e.getMessage());
        }
    }

    @Test
    public void testExcelStudentOrGrupaEmpty() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(inputStream, "Fisierul Excel nu a fost gasit!");

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int studentColIndex = -1;
            int grupaColIndex = -1;
            int nrColIndex = -1;

            // Luam prima linie ca header
            Row headerRow = sheet.getRow(8); // Linia 9 in Excel (index 8 in Java)
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

            assertTrue(studentColIndex != -1, "Coloana 'Student' nu a fost gasita!");
            assertTrue(grupaColIndex != -1, "Coloana 'Grupa' nu a fost gasita!");
            assertTrue(nrColIndex != -1, "Coloana 'Nr' nu a fost gasita!");

            boolean hasEmpty = false;

            // Iteram de la linia 9 (index 8 in Java) pana la prima valoare null in coloana 'Nr'
            for (int i = 9; i <= sheet.getLastRowNum(); i++) { // Linia 9 in Excel (index 8 in Java)
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell nrCell = row.getCell(nrColIndex);
                if (nrCell == null || nrCell.getCellType() == CellType.BLANK) {
                    // Daca intalnim o celula goala in coloana 'Nr', opriți iterația
                    break;
                }

                // Verificam daca exista celule goale in coloanele 'Student' sau 'Grupa'
                Cell studentCell = row.getCell(studentColIndex);
                Cell grupaCell = row.getCell(grupaColIndex);

                if (isCellEmpty(studentCell) || isCellEmpty(grupaCell)) {
                    hasEmpty = true;
                    break;
                }
            }

            assertFalse(hasEmpty, "Exista celule goale in coloanele Student sau Grupa!");

        } catch (Exception e) {
            fail("Exceptie la citirea fisierului: " + e.getMessage());
        }
    }

    private boolean isCellEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK ||
                (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }


    // Next test: Verific daca o grupa are sau nu un sef de grupa.

    @Test
    public void testGrupaAreSefDeGrupa() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            assertNotNull(inputStream, "Fisierul Excel nu a fost gasit!");

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            int studentColIndex = -1;
            int grupaColIndex = -1;
            int nrColIndex = -1;
            int obsColIndex = -1;

            // Luam prima linie ca header (linia 9 in Excel este index 8 in Java)
            Row headerRow = sheet.getRow(8); // Presupunem ca header-ul este pe linia 9 (index 8)
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

            // Asiguram ca toate coloanele necesare sunt gasite
            assertTrue(studentColIndex != -1, "Coloana 'Student' nu a fost gasita!");
            assertTrue(grupaColIndex != -1, "Coloana 'Grupa' nu a fost gasita!");
            assertTrue(nrColIndex != -1, "Coloana 'Nr.' nu a fost gasita!");
            assertTrue(obsColIndex != -1, "Coloana 'Obs.' nu a fost gasita!");

            boolean allGroupsHaveSef = true;
            boolean groupHasSefDeGrupa = false;

            // Iteram prin fiecare linie a fisierului
            for (int i = 9; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue; // Sarim peste liniile goale

                // Extragem valoarea din coloana „Nr.” pentru a verifica daca este 1 (inceputul unei noi grupe)
                Cell nrCell = row.getCell(nrColIndex);
                if (nrCell == null || nrCell.getCellType() == CellType.BLANK) {
                    // Daca intalnim o celula null sau goala in coloana „Nr.”, inseamna ca am ajuns la sfarsitul fisierului
                    break; // Oprirea procesului
                }

                String nrValue = getCellValueAsString(nrCell);
                if ("1".equals(nrValue)) { // Daca intalnim 1 in coloana „Nr.”, inseamna ca este inceputul unei noi grupe
                    if (!groupHasSefDeGrupa) {
                        // Daca grupa anterioara nu avea sef de grupa
                        System.out.println("Grupa de la linia " + i + " nu are sef de grupa!");
                        allGroupsHaveSef = false; // Semnalam ca exista o grupa fara sef de grupa
                    }
                    groupHasSefDeGrupa = false; // Resetam pentru urmatoarea grupa
                }

                // Verificam daca exista textul „sef de grupa” in coloana „Obs.”
                Cell obsCell = row.getCell(obsColIndex);
                if (obsCell != null) {
                    String obsValue = getCellValueAsString(obsCell);
                    if (obsValue.toLowerCase().contains("șef de grupă")) {
                        groupHasSefDeGrupa = true;
                    }
                }
            }

//             Verificam ultima grupa
//            if (!groupHasSefDeGrupa) {
//                System.out.println("Ultima grupa nu are sef de grupa!");
//                allGroupsHaveSef = false;
//            }

            // Asiguram ca toate grupele au sef de grupa
            assertTrue(allGroupsHaveSef, "Nu toate grupele au sef de grupa!");

        } catch (Exception e) {
            fail("Exceptie la citirea fisierului: " + e.getMessage());
        }
    }

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











