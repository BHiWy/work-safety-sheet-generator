package org.generator.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.generator.entities.Student;

/**
 * Data Transfer Object (DTO) for the {@link Student} entity.
 * Used to transfer student data between different layers of the application.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private String firstName;
    private String lastName;
    private Integer year;
    private String email;
    private String paternalInitial;
}
