package org.generator.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.generator.entities.Student;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class ExcelService {
    private static final String FILE_PATH = "grupe-an-III-AIA-2024_2025.xls";

    public void readExcel() {
        List<Student> students = new ArrayList<>();

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
             Workbook workbook;
            if (FILE_PATH.endsWith(".xls")) {
            workbook = new HSSFWorkbook(fis); // Fișier .xls
        } else if (FILE_PATH.endsWith(".xlsx")) {

            workbook = new XSSFWorkbook(fis); // Fișier .xlsx
        } else {
                System.err.println("Unsupported file format: " + FILE_PATH);
                return;
            }

            Sheet sheet = workbook.getSheetAt(0); // Prima foaie a Excelului

            // Alternativă la for clasica
            IntStream.rangeClosed(10, sheet.getLastRowNum()) // Sarim peste antet (rând 0)
                    .mapToObj(sheet::getRow) // Convertim indexul intr-un rand
                    .filter(Objects::nonNull) // Evitam randurile nule
                    .forEach(row -> {
                        Cell groupCell = row.getCell(8); // A doua coloana (index 1)
                        Cell studentCell = row.getCell(1); // A noua coloana (index 8)

                        if (groupCell != null && studentCell != null) {
                            String group = groupCell.getStringCellValue().trim();
                            String student = studentCell.getStringCellValue().trim();
                            students.add(new Student(student, group));
                        }
                    });

        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Afisam studentii citiți
        students.forEach(System.out::println);

    }
}
