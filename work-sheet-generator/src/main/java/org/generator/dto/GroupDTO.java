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
public class GroupDTO {

    private String code;
    private List<StudentDTO> students;
    private Integer year;
    private StudentDTO groupLeader;
}
