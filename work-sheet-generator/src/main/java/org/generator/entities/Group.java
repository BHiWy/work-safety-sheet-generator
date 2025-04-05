package org.generator.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;

@Entity
@Table(name="UNI_GROUPS")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "UNI_GROUPS_STUDENTS",
            joinColumns = @JoinColumn(name = "GROUP_ID"),
            inverseJoinColumns = @JoinColumn(name = "STUDENTS_ID")
    )
    private ArrayList<Student> students = new ArrayList<>();

    @Column(name = "YEAR")
    private Integer year;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "GROUP_LEADER", referencedColumnName = "id")
    private Student groupLeader;
}