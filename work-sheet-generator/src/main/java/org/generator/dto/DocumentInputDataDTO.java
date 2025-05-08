package org.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentInputDataDTO {
    private String professorName;
    private String courseName;
    private String assistantName;
    private String place;
    private List<GroupDTO> groups;
}
