package org.generator.classes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Student {
    private String firstName;
    private String lastName;
    private Integer year;
    private String email;
    private String paternalInitial;

    // Constructor fara parametrii
    public Student() {

    }

    // Constructor cu parametrii
    public Student(String firstName, String lastName, Integer year, String email, String paternalInitial) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.year = year;
        this.email = email;
        this.paternalInitial = paternalInitial;
    }
}





