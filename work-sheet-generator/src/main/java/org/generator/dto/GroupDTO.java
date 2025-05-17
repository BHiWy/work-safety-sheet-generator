package org.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.generator.entities.Group;

import java.util.List;

/**
 * Data Transfer Object (DTO) for the {@link Group} entity.
 * Used to transfer group data between different layers of the application.
 */
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
