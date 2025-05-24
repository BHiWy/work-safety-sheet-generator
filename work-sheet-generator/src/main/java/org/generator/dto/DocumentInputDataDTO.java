package org.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing the input data required to generate a document.
 * Used in the request body of the /word endpoint.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInputDataDTO {
    private String professorName;
    private String courseName;
    private String assistantName;
    private String place;
    private LocalDate fromDate;
    private LocalDate toDate;
    private List<GroupDTO> groups;
}
