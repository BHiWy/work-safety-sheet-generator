package org.generator.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.generator.dto.DocumentInputDataDTO;
import org.generator.dto.StudentDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class responsible for generating Word documents (.docx)
 * based on a predefined template (FisaProtectiaMuncii.docx).
 * <p>
 * The service loads the template from the classpath and replaces
 * placeholder fields such as ${fromDate}, ${nume}, and ${studentNumber}
 * with actual data fetched from the database using the {@link GroupService}.
 * <p>
 * The generated document includes group-specific details, professor and assistant info,
 * and a dynamically populated student table.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final GroupService groupService;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Generates a .docx Word document using a preloaded template and populates it
     * with data such as course info, group details, and a student list.
     * <p>
     * Placeholders inside the template are dynamically replaced with current values.
     * The method handles all student groups listed in the input DTO and returns the
     * document as a {@link ByteArrayOutputStream}.
     *
     * @param response  the HTTP response (not used for writing inside this method)
     * @param inputData the {@link DocumentInputDataDTO} containing input values such as professor, assistant, and groups
     * @return {@link ByteArrayOutputStream} containing the generated Word document
     * @throws IOException if the template cannot be loaded or document writing fails
     */
    public ByteArrayOutputStream generateWorkSheet(HttpServletResponse response, DocumentInputDataDTO inputData) throws IOException {
        InputStream templateStream = new ClassPathResource("FisaProtectiaMuncii.docx").getInputStream();
        XWPFDocument document = new XWPFDocument(templateStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = fromDate.plusMonths(6);

        for (var group : inputData.getGroups()) {
            List<StudentDTO> students = groupService.findStudentsByGroupCode(group.getCode());

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("${fromDate}", FORMATTER.format(fromDate));
            placeholders.put("${toDate}", FORMATTER.format(toDate));
            placeholders.put("${nume}", inputData.getProfessorName());
            placeholders.put("${functie}", "Profesor");
            placeholders.put("${assistant}", inputData.getAssistantName());
            placeholders.put("${nrStudenti}", String.valueOf(students.size()));
            placeholders.put("${cod}", group.getCode());
            placeholders.put("${curs}", inputData.getCourseName());
            placeholders.put("${lab}", "Laborator");

            replacePlaceholders(document, placeholders);
            replaceStudentTablePlaceholder(document, students);
        }

        document.write(out);
        document.close();
        return out;
    }

    /**
     * Replaces all placeholder text in both paragraphs and table cells
     * of the provided {@link XWPFDocument} using the given key-value map.
     *
     * @param document     the Word document to operate on
     * @param placeholders a {@link Map} of placeholder keys and their corresponding replacement values
     */
    private void replacePlaceholders(XWPFDocument document, Map<String, String> placeholders) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceInParagraph(paragraph, placeholders);
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                        replaceInParagraph(paragraph, placeholders);
                    }
                }
            }
        }
    }

    /**
     * Replaces placeholder keys inside a single paragraph using the given map.
     *
     * @param paragraph    the {@link XWPFParagraph} to process
     * @param placeholders the key-value map of replacements
     */
    private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> placeholders) {
        for (XWPFRun run : paragraph.getRuns()) {
            String text = run.getText(0);
            if (text != null) {
                for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                    if (text.contains(entry.getKey())) {
                        text = text.replace(entry.getKey(), entry.getValue());
                    }
                }
                run.setText(text, 0);
            }
        }
    }

    /**
     * Detects the `${studentNumber}` placeholder and replaces it with a
     * dynamically generated table listing each student in the group.
     *
     * @param document the Word document to update
     * @param students a {@link List} of {@link StudentDTO} representing the students to include
     */
    private void replaceStudentTablePlaceholder(XWPFDocument document, List<StudentDTO> students) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (int i = 0; i < paragraphs.size(); i++) {
            XWPFParagraph paragraph = paragraphs.get(i);
            String text = paragraph.getText();
            if (text != null && text.contains("${studentNumber}")) {
                // Create cursor at this paragraph's location
                org.apache.xmlbeans.XmlCursor cursor = paragraph.getCTP().newCursor();

                // Insert table at cursor
                XWPFTable table = document.insertNewTbl(cursor);

                // Header
                XWPFTableRow header = table.createRow();
                header.getCell(0).setText("Nr.");
                header.addNewTableCell().setText("Numele și prenumele");
                header.addNewTableCell().setText("Semnătura");

                // Rows
                for (int j = 0; j < students.size(); j++) {
                    StudentDTO s = students.get(j);
                    String fullName = s.getLastName() + " " +
                            (s.getPaternalInitial() != null ? s.getPaternalInitial() + ". " : "") +
                            s.getFirstName();

                    XWPFTableRow row = table.createRow();
                    row.getCell(0).setText(String.valueOf(j + 1));
                    row.getCell(1).setText(fullName);
                    row.getCell(2).setText("");
                }

                // Remove the placeholder paragraph
                document.removeBodyElement(document.getPosOfParagraph(paragraph));
                break;
            }
        }
    }

}
