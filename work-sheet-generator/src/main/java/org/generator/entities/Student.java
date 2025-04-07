package org.generator.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="STUDENTS")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PATERNAL_INITIAL")
    private String paternalInitial;
}