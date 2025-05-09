package org.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.generator.entities.Group;

import java.util.List;
import java.util.stream.Collectors;


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

    /**
     * Constructor to create a {@link GroupDTO} object from a {@link Group} entity.
     * It maps the fields from the entity to the DTO.
     * @param group - The `{@link Group} entity to create the DTO from.
     */
    public GroupDTO(Group group) {
        this.code = group.getCode();
        this.groupLeader = new StudentDTO(group.getGroupLeader());
        this.year = group.getYear();
        this.students = group.getStudents().stream().map(StudentDTO::new).collect(Collectors.toList());
    }
}
