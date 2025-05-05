package org.generator.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.generator.dto.GroupDTO;
import org.generator.dto.StudentDTO;
import org.generator.entities.Student;
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

    /**
     * Constructor for {@code ExcelService} which injects the {@link GroupService} and {@link StudentService}.
     *
     * @param studentService the service responsible for handling student-related operations.
     * @param groupService the service responsible for handling group-related operations.
     */
    public ExcelService(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
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
}
