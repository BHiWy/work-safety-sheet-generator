package org.generator.service;


import org.apache.commons.collections4.Factory;
import org.apache.poi.ss.usermodel.*;
import org.generator.entities.Student;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ExcelService1 {
    private static final String FILE_PATH = "../resources/grupe-an-III-AIA-2024-2025.xls";

    public void readExcel() {
        List<Student> students = new ArrayList<>();

        Factory<Object> XSSFWorkbookFactory = null;
        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = (Workbook) XSSFWorkbookFactory.create()) {

            Sheet sheet = workbook.getSheetAt(0); // Prima foaie a Excelului

            // Alternativă la for clasica
            IntStream.rangeClosed(1, sheet.getLastRowNum()) // Sărim peste antet (rând 0)
                    .mapToObj(sheet::getRow) // Convertim indexul într-un rând
                    .filter(row -> row != null) // Evităm rândurile nule
                    .forEach(row -> {
                        Cell groupCell = row.getCell(1); // A doua coloană (index 1)
                        Cell studentCell = row.getCell(8); // A noua coloană (index 8)

                        if (groupCell != null && studentCell != null) {
                            String group = groupCell.getStringCellValue();
                            String student = studentCell.getStringCellValue();
                            students.add(new Student(student, group));
                        }
                    });

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Afișăm studenții citiți
        students.forEach(System.out::println);
    }
}
