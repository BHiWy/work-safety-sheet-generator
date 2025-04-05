package org.generator.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="STUDENTS")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "year")
    private Integer year;

    @Column(name = "email")
    private String email;

    @Column(name = "paternal_initial")
    private String paternalInitial;
}