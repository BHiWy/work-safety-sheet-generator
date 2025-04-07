package org.generator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.generator.entities.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 *
 */
@Transactional
@Service
@Slf4j
public class ExcelService {
    private static final String FILE_PATH = "files/grupe-an-III-AIA-2024_2025.xls";

    @Autowired
    public void ProfessorService(StudentService studentService) {
    }

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

            Sheet sheet = workbook.getSheetAt(0); // First sheet of the Excel file

            // Alternative to the classic for loop
            IntStream.rangeClosed(10, sheet.getLastRowNum()) // Skip the header (row 0)
                    .mapToObj(sheet::getRow) // Convert the index to a row
                    .filter(Objects::nonNull) // Avoid null rows
                    .forEach(row -> {
                        Cell groupCell = row.getCell(8); // The second column (index 1)
                        Cell studentCell = row.getCell(1); // Ninth column (index 8)

                        if (groupCell != null && studentCell != null) {
                            String group = groupCell.getStringCellValue().trim();
                            String student = studentCell.getStringCellValue().trim();
                            // !!!!! rezolvare cu db save student
//                            students.add(new Student(student, group));
                        }
                    });

        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Display the read students
        students.forEach(System.out::println);

    }
}
