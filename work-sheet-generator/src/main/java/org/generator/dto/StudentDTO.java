package org.generator.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
