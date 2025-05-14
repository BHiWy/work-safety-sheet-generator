package org.generator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.generator.entities.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.IntStream;

@Transactional
@Service
@Slf4j
public class ProfessorExcelService {

    private static final String FILE_PATH = "files/profesori.xlsx";

    private final ProfessorService professorService;

    @Autowired
    public ProfessorExcelService(ProfessorService professorService) {
        this.professorService = professorService;
    }

    public void readProfessorsFromExcel() {
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

            Sheet sheet = workbook.getSheetAt(0);

            IntStream.rangeClosed(1, sheet.getLastRowNum())
                    .mapToObj(sheet::getRow)
                    .filter(Objects::nonNull)
                    .forEach(row -> {
                        Cell fullNameCell = row.getCell(0); // Coloana 0 - Full Name
                        Cell courseCell = row.getCell(1);   // Coloana 1 - Course
                        Cell emailCell = row.getCell(2);    // Coloana 2 - Email
                        Cell rankCell = row.getCell(3);     // Coloana 3 - Rank

                        if (fullNameCell != null && courseCell != null) {
                            String fullName = fullNameCell.getStringCellValue().trim();
                            String course = courseCell.getStringCellValue().trim();
                            String email = (emailCell != null) ? emailCell.getStringCellValue().trim() : null;
                            String rank = (rankCell != null) ? rankCell.getStringCellValue().trim() : null;

                            Professor professor = new Professor();
                            professor.setFullName(fullName);
                            professor.setCourses(Collections.singletonList(course)); // o singură materie acum
                            professor.setEmail(email);
                            professor.setRank(rank);

                            professorService.save(professor);

                            System.out.println("Saved Professor: " + professor);
                        }
                    });

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}