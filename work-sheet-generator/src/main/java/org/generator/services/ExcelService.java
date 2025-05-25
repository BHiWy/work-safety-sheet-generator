package org.generator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.entities.Student;
import org.generator.entities.Professor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Service class responsible for reading and processing data from Excel files.
 * The {@link #readStudentsFromExcel()} method handles the extraction of student and group information,
 * ensuring uniqueness and associating group leaders. The processed data is then saved
 * using the {@link GroupService}.
 */
@Transactional
@Service
@Slf4j
public class ExcelService {
    private static final String studentsExcelPath = "files/grupe-an-III-AIA-2024_20255.xls";
    private static final String professorsExcelPath = "files/profesori.xls";

    private final StudentService studentService;
    private final GroupService groupService;
    private final ProfessorService professorService;

    /**
     * Constructor for {@code ExcelService} which injects the {@link GroupService} and {@link StudentService}.
     *
     * @param studentService the service responsible for handling student-related operations.
     * @param groupService the service responsible for handling group-related operations.
     */
    public ExcelService(StudentService studentService, GroupService groupService, ProfessorService professorService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.professorService = professorService;
    }

    /**
     * Reads student and group data from an Excel file specified by {@link #studentsExcelPath}.
     * Extracts student names and group codes, creating unique {@link StudentDTO} and {@link #studentsExcelPath} objects.
     * Associates students with their groups and identifies group leaders.
     * Saves all unique groups (and associated students) using {@link GroupService#save(GroupDTO)}.
     * This method takes no parameters and returns no value.
     */
    @Transactional
    public void readStudentsFromExcel() {
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(studentsExcelPath)) {
            Workbook workbook;
            if (studentsExcelPath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (studentsExcelPath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                System.err.println("Unsupported file format: " + studentsExcelPath);
                return;
            }

            Sheet sheet = workbook.getSheetAt(0);

            Map<String, GroupDTO> uniqueGroups = new HashMap<>();
            Map<String, StudentDTO> uniqueStudents = new HashMap<>();

            IntStream.rangeClosed(9, sheet.getLastRowNum()).mapToObj(sheet::getRow).filter(Objects::nonNull).forEach(row -> {
                if (row.getCell(8) != null && row.getCell(1) != null) {
                    String groupCode = row.getCell(8).getStringCellValue().trim();
                    String studentName = row.getCell(1).getStringCellValue().trim();
                    String groupLeaderName = row.getCell(7) != null ? row.getCell(7).getStringCellValue().trim() : "";

                    GroupDTO groupDTO = uniqueGroups.computeIfAbsent(groupCode, code -> {
                        GroupDTO newGroup = new GroupDTO();
                        newGroup.setCode(code);
                        newGroup.setStudents(new ArrayList<>());
                        newGroup.setYear(this.groupService.extractYear(groupCode));
                        return newGroup;
                    });

                    StudentDTO studentDTO = uniqueStudents.computeIfAbsent(studentName, name -> {
                        StudentDTO dto = new StudentDTO();
                        dto.setFirstName(name);
                        dto.setEmail(this.studentService.createEmail(name));
                        dto.setYear(this.groupService.extractYear(groupCode));
                        return dto;
                    });

                    Student extractedStudentDetails = this.studentService.extractNameDetails(studentName);
                    studentDTO.setLastName(extractedStudentDetails.getLastName());
                    studentDTO.setFirstName(extractedStudentDetails.getFirstName());
                    studentDTO.setPaternalInitial(extractedStudentDetails.getPaternalInitial());

                    if (!groupDTO.getStudents().contains(studentDTO)) {
                        groupDTO.getStudents().add(studentDTO);
                    }

                    if (!groupLeaderName.isEmpty() && groupDTO.getGroupLeader() == null) {
                        StudentDTO leaderDTO = uniqueStudents.get(studentName);
                        if (leaderDTO != null) {
                            groupDTO.setGroupLeader(leaderDTO);
                        }
                    }
                }
            });

            for (GroupDTO groupDTO : uniqueGroups.values()) {
                groupService.save(groupDTO);
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads professor and assistant data from an Excel file specified by {@link #professorsExcelPath}.
     * Extracts professor full names, associated courses, and assistant names from each row.
     * Creates {@link Professor} entities for both professors and their assistants, assigning the correct rank ("Profesor" or "Asistent").
     * Saves all created {@link Professor} entities using {@link ProfessorService#save(Professor)}.
     * It handles potential {@link IOException} during file reading and logs any errors encountered.
     */
    @Transactional
    public void readProfessorsFromExcel() {

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(professorsExcelPath)) {
            Workbook workbook = professorsExcelPath.endsWith(".xls") ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            IntStream.rangeClosed(1, sheet.getLastRowNum())
                    .mapToObj(sheet::getRow)
                    .filter(Objects::nonNull)
                    .forEach(row -> {
                        String fullName = getCellStringValue(row.getCell(2));
                        String course = getCellStringValue(row.getCell(3));
                        String assistantsRaw = getCellStringValue(row.getCell(4));

                        if (fullName.isEmpty() && course.isEmpty()) {
                            return;
                        }

                        Professor professor = new Professor();
                        professor.setFullName(fullName);
                        professor.setCourses(List.of(course));
                        professor.setRank("Profesor");
                        professorService.save(professor);

                        Arrays.stream(assistantsRaw.split("\n"))
                                .map(String::trim)
                                .filter(name -> !name.isEmpty())
                                .map(name -> {
                                    Professor assistant = new Professor();
                                    assistant.setFullName(name);
                                    assistant.setCourses(List.of(course));
                                    assistant.setRank("Asistent");
                                    return assistant;
                                })
                                .forEach(professorService::save);
                    });

        } catch (IOException e) {
            log.error("Failed to read professors from Excel: {}", e.getMessage());
        }
    }

    /**
     * Retrieves the string value of a given Excel cell.
     * It converts the cell's content to a string and removes leading and trailing whitespace.
     *
     * @param cell The Excel {@link Cell} to retrieve the value from.
     * @return The trimmed string value of the cell, or an empty string if the cell is null.
     */
    private String getCellStringValue(Cell cell) {
        return (cell == null) ? "" : cell.toString().trim();
    }
}
