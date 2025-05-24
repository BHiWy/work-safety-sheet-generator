package org.generator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.generator.dto.DocumentInputDataDTO;
import org.generator.dto.GroupDTO;
import org.generator.entities.Group;
import org.generator.entities.Student;
import org.generator.mapper.GroupMapper;
import org.springframework.stereotype.Service;
import pl.jsolve.templ4docx.core.Docx;
import pl.jsolve.templ4docx.core.VariablePattern;
import pl.jsolve.templ4docx.variable.TableVariable;
import pl.jsolve.templ4docx.variable.TextVariable;
import pl.jsolve.templ4docx.variable.Variable;
import pl.jsolve.templ4docx.variable.Variables;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for generating Word documents (.docx) from a predefined template.
 * <p>
 * This class handles the creation of work sheet document
 * by populating placeholders with dynamic data such as course details, date ranges, professor/assistant info,
 * group codes, and student information.
 * <p>
 * Data is fetched and structured using {@link GroupMapper} for the groups,
 * and the final document is built using a template engine that supports variable substitution and table generation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final GroupMapper groupMapper;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Generates a Word document from a template by populating placeholders
     * with data from the provided {@link DocumentInputDataDTO}, including course info,
     * date range, staff names, group codes, and a list of students.
     * <p>
     * Student data is compiled into a dynamic table. The final document is returned
     * as a {@link ByteArrayOutputStream}.
     *
     * @param inputData the input data containing professor, assistant, group and course details
     * @return the generated Word document as a {@link ByteArrayOutputStream}
     * @throws IOException if the template fails to load or the document cannot be written
     */
    public ByteArrayOutputStream generateWorkSheet(DocumentInputDataDTO inputData) throws IOException {
        InputStream template = getClass().getClassLoader().getResourceAsStream("files/FisaProtectiaMuncii.docx");
        if (template == null){
            return new ByteArrayOutputStream(0);
        }
        Docx docx = new Docx(template);
        template.close();
        docx.setVariablePattern(new VariablePattern("${", "}"));

        Variables var = new Variables();

        var.addTextVariable(new TextVariable("${fromDate}", inputData.getFromDate().format(dateFormatter)));
        var.addTextVariable(new TextVariable("${toDate}", inputData.getToDate().format(dateFormatter)));
        var.addTextVariable(new TextVariable("${professorName}", inputData.getProfessorName()));
        var.addTextVariable(new TextVariable("${assistantName}", inputData.getAssistantName()));

        if (inputData.getGroups().size() == 1) {
            var.addTextVariable(new TextVariable("${groupCode}", inputData.getGroups().get(0).getCode()));
        } else {
            String combinedGroupCodes = inputData.getGroups().stream()
                    .map(GroupDTO::getCode)
                    .collect(Collectors.joining(" + "));
            var.addTextVariable(new TextVariable("${groupCode}", combinedGroupCodes));
        }
        var.addTextVariable(new TextVariable("${course}", inputData.getCourseName()));
        var.addTextVariable(new TextVariable("${place}", inputData.getPlace()));

        TableVariable studentsTable = new TableVariable();
        List<Variable> nrStud = new ArrayList<>();
        List<Variable> studentInfo = new ArrayList<>();

        int studentCounter = 1;

        for(GroupDTO group: inputData.getGroups()){
            Group entityGroup = this.groupMapper.toEntity(group);
            for (Student student: entityGroup.getStudents()){

                nrStud.add(new TextVariable("${nrStud}", String.valueOf(studentCounter)));
                studentInfo.add(new TextVariable("${studentInfo}", student.getLastName() + " " + student.getPaternalInitial() + " " + student.getFirstName()));

                studentCounter++;
            }
        }
        var.addTextVariable(new TextVariable("${lastNrStud}", String.valueOf(studentCounter - 1)));
        studentsTable.addVariable(nrStud);
        studentsTable.addVariable(studentInfo);

        var.addTableVariable(studentsTable);
        docx.fillTemplate(var);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        docx.save(out);

        return out;
    }
}