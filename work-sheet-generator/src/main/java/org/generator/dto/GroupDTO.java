package org.generator.dto;

import java.util.ArrayList;

public class GroupDTO {
    private String code;
    private ArrayList<StudentDTO> students;
    private Integer year;
    private StudentDTO groupLeader;

    // Constructor fara argumente
    public GroupDTO() {}

    // Constructor
    public GroupDTO(String code, ArrayList<StudentDTO> students, Integer year, StudentDTO groupLeader) {
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

    public ArrayList<StudentDTO> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentDTO> students) {
        this.students = students;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public StudentDTO getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(StudentDTO groupLeader) {
        this.groupLeader = groupLeader;
    }
}
