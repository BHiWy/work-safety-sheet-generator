package org.generator.dto;

public class StudentDTO {
    private String firstName;
    private String lastName;
    private Integer year;
    private String email;
    private String paternalInitial;

    // Constructor fara parametrii
    public StudentDTO() {

    }
    // Constructor cu parametrii
    public StudentDTO(String firstName, String lastName, Integer year, String email, String paternalInitial) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.year = year;
        this.email = email;
        this.paternalInitial = paternalInitial;
    }

    // Setteri si Getteri
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
