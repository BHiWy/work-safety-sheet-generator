package org.generator.entities;

public class Student {
    private String nume;
    private String prenume;
    private Integer an;
    private String email;
    private String initialaTatalui;
    private String group;
    // Constructor fara parametrii
    public Student() {

    }

    // Constructor cu parametrii
    public Student(String nume, String prenume, Integer an, String email, String initialaTatalui) {
        this.nume = nume;
        this.prenume = prenume;
        this.an = an;
        this.email = email;
        this.initialaTatalui = initialaTatalui;
    }

    public Student(String nume, String group)
    {
        this.nume=nume;
        this.group=group;
    }


    // Setteri si Getteri

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getAn() {
        return an;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setInitialaTatalui(String initialaTatalui) {
        this.initialaTatalui = initialaTatalui;
    }

    public String getInitialaTatalui() {
        return initialaTatalui;
    }
}





