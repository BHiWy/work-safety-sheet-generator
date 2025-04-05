package org.generator.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;

@Entity
@Table(name="GROUPS")
@Data
public class Group {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="code", nullable=false)
    private String code;

    @OneToMany(fetch =FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="REF_STUDENTS")
    private ArrayList<Student> students;

    @Column(name="YEAR")
    private Integer year;


    @OneToOne(fetch =FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name="GROUP_LEADER", referencedColumnName = "id")
    private Student groupLeader;

    // Constructor fara argumente
    public Group() {}

    // Constructor
    public Group(String code, ArrayList<Student> students, Integer year, Student groupLeader) {
        this.code = code;
        this.students = students;
        this.year = year;
        this.groupLeader = groupLeader;
    }

    // Gettere si settere
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Student getGrorpLeader() {
        return groupLeader;
    }

    public void setGroupLeader(Student groupLeader) {
        this.groupLeader = groupLeader;
    }
}
