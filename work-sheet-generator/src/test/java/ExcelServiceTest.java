

import org.generator.service.ExcelService;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;


public class ExcelServiceTest {

    @Test
    public void testReadExcel_validFile() {
        // Verificam ca fisierul de test exista in resources
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("grupe-an-III-AIA-2024_2025.xls")) {
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
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("non_existent_file.xls")) {
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
        try (InputStream mockInputStream = getClass().getClassLoader().getResourceAsStream("empty_test_file.xls")) {
            assertNotNull(mockInputStream, "Fisierul gol nu a fost gasit!");

            ExcelService excelService = new ExcelService();
            excelService.readExcel();

            // Aici, doar verificam ca nu a aparut nicio eroare, iar fisierul gol nu a cauzat un crash
        } catch (Exception e) {
            fail("Testul a esuat din cauza unei erori: " + e.getMessage());
        }
    }
}
