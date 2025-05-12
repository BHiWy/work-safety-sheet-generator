package org.generator.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.generator.dto.DocumentInputDataDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Service class responsible for generating a Word document (.docx)
 * that represents the Occupational Safety Sheet (Fisa de protectia muncii).
 * The generation is done using Apache POI without any external template engines.
 * <p>
 * The document is generated programmatically based on the input data received,
 * or default fallback values are used when data is missing.
 */
@Slf4j
@Service
public class WordService {

    /**
     * Generates an Occupational Safety Sheet (.docx) file using Apache POI.
     * This method creates a new Word document from scratch and populates it
     * with personal and job-related information from the provided DTO.
     * In case some fields are null, default values or placeholders are used for testing.
     *
     * @param inputData a DTO object containing the required data fields
     * @return ByteArrayOutputStream containing the generated Word document
     * @throws IOException if the document cannot be generated or written
     */
    public ByteArrayOutputStream generateWorkSheet(HttpServletResponse response, DocumentInputDataDTO inputData) throws IOException {
        XWPFDocument document = new XWPFDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Create and format the document title
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun runTitle = title.createRun();
        runTitle.setText("Occupational Safety Sheet");
        runTitle.setBold(true);
        runTitle.setFontSize(16);

        document.createParagraph(); // Empty line for spacing

        // Insert each field as a labeled paragraph
        addField(document, "Professor", inputData.getProfessorName() != null ? inputData.getProfessorName() : "TestProfessor");
        addField(document, "Course", inputData.getCourseName() != null ? inputData.getCourseName() : "TestCourse");
        addField(document, "Assistant", inputData.getAssistantName() != null ? inputData.getAssistantName() : "TestAssistant");
        addField(document, "Place", inputData.getPlace() != null ? inputData.getPlace() : "TestPlace");

        // Write document to output stream and close
        document.write(out);
        document.close();

        return out;
    }

    /**
     * Helper method that adds a labeled field and its value as a new paragraph in the document.
     * Used to insert data lines like "First Name: John".
     *
     * @param doc   the Word document object
     * @param label the field label (e.g., "First Name")
     * @param value the value to be displayed
     */
    private void addField(XWPFDocument doc, String label, String value) {
        XWPFParagraph para = doc.createParagraph();
        XWPFRun run = para.createRun();
        run.setText(label + ": " + value);
        run.setFontSize(12);
    }
}