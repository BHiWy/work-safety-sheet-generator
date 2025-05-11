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
 * The {@link #readExcel()} method handles the extraction of student and group information,
 * ensuring uniqueness and associating group leaders. The processed data is then saved
 * using the {@link GroupService}.
 */
@Transactional
@Service
@Slf4j
public class ExcelService {
    private static final String FILE_PATH = "files/grupe-an-III-AIA-2024_2025.xls";
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
     * Reads student and group data from an Excel file specified by {@link #FILE_PATH}.
     * Extracts student names and group codes, creating unique {@link StudentDTO} and {@link #FILE_PATH} objects.
     * Associates students with their groups and identifies group leaders.
     * Saves all unique groups (and associated students) using {@link GroupService#save(GroupDTO)}.
     * This method takes no parameters and returns no value.
     */
    @Transactional
    public void readExcel() {
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(FILE_PATH)) {
            Workbook workbook;
            if (FILE_PATH.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else if (FILE_PATH.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else {
                System.err.println("Unsupported file format: " + FILE_PATH);
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

                    // Extract the name details and set them in the StudentDTO object
                    Student extractedStudentDetails = this.studentService.extractNameDetails(studentName);
                    studentDTO.setLastName(extractedStudentDetails.getLastName());
                    studentDTO.setFirstName(extractedStudentDetails.getFirstName());
                    studentDTO.setPaternalInitial(extractedStudentDetails.getPaternalInitial());

                    // Associate the student with the group
                    if (!groupDTO.getStudents().contains(studentDTO)) {
                        groupDTO.getStudents().add(studentDTO);
                    }

                    // Check if the student is the group leader
                    if (!groupLeaderName.isEmpty() && groupDTO.getGroupLeader() == null) {
                        StudentDTO leaderDTO = uniqueStudents.get(studentName);
                        if (leaderDTO != null) {
                            groupDTO.setGroupLeader(leaderDTO);
                        }
                    }
                }
            });

            // Save groups (and students by default)
            for (GroupDTO groupDTO : uniqueGroups.values()) {
                groupService.save(groupDTO);
            }

        } catch (IOException | NullPointerException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads professors and assistant professors from an Excel file and persists them as Professor entities.
     *
     * Expected columns in the Excel file:
     * - Column 0: Full name of the professor (main instructor)
     * - Column 1: Course taught
     * - Column 2: Assistant professors (optional, multiple names separated by newlines)
     *
     * The method creates separate Professor entities for assistants,
     * assigning them the rank "Asistent", and for main professors with rank "Profesor".
     */
    @Transactional
    public void readProfessorsFromExcel() {
        String filePath = "files/profesori.xls"; // Update if the filename is different

        try (InputStream fis = getClass().getClassLoader().getResourceAsStream(filePath)) {
            Workbook workbook = filePath.endsWith(".xls") ? new HSSFWorkbook(fis) : new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String fullName = getCellStringValue(row.getCell(0));
                String course = getCellStringValue(row.getCell(1));
                String assistantsRaw = getCellStringValue(row.getCell(2));

                // Skip row if essential data is missing
                if (fullName.isEmpty() || course.isEmpty()) continue;

                // Main professor
                Professor professor = new Professor();
                professor.setFullName(fullName);
                professor.setCourses(List.of(course));
                professor.setRank("Profesor");

                professorService.save(professor);

                // Assistant professors
                List<Professor> assistants = Arrays.stream(assistantsRaw.split("\n"))
                        .map(String::trim)
                        .filter(name -> !name.isEmpty())
                        .map(name -> {
                            Professor assistant = new Professor();
                            assistant.setFullName(name);
                            assistant.setCourses(List.of(course)); // Assistants associated with same course
                            assistant.setRank("Asistent");
                            return assistant;
                        })
                        .toList();

                assistants.forEach(professorService::save);
            }

        } catch (IOException e) {
            log.error("Failed to read professors from Excel: {}", e.getMessage());
        }
    }

    /**
     * Utility method to safely extract string value from a cell.
     *
     * @param cell the Excel cell
     * @return the trimmed string value, or an empty string if null
     */
    private String getCellStringValue(Cell cell) {
        return (cell == null) ? "" : cell.toString().trim();
    }
}
