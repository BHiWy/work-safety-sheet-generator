package org.generator.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="PROFESSORS")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "COURSES")
    @ElementCollection
    private List<String> courses;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "RANK_TITLE")
    private String rank;
}

