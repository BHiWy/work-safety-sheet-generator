package org.generator.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name="STUDENTS")
@Data
public class Student implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="year")
    private Integer year;

    @Column(name="email")
    private String email;

    @Column(name="paternal_initial")
    private String paternalInitial;


    public Student() { }


    public Student(String firstName, String lastName, Integer year, String email, String paternalInitial) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.year = year;
        this.email = email;
        this.paternalInitial = paternalInitial;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPaternalInitial(String paternalInitial) {
        this.paternalInitial = paternalInitial;
    }

    public String getPaternalInitial() {
        return paternalInitial;
    }
}
