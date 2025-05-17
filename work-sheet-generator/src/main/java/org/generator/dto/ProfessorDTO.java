package org.generator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.generator.entities.Professor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for the {@link Professor} entity.
 * Used to transfer professor data between different layers of the application.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {
    private long id;
    private String fullName;
    private List<String> courses;
    private String email;
    private String rank;
}
