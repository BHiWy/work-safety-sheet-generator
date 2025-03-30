package entities;

import java.util.List;

public class Grupa {
    private String cod;
    private List<Student> studenti;
    private Integer an;
    private Student sefDeGrupa;

    // Constructor fara argumente
    public Grupa() {}

    // Constructor
    public Grupa(String cod, List<Student> studenti, Integer an, Student sefDeGrupa) {
        this.cod = cod;
        this.studenti = studenti;
        this.an = an;
        this.sefDeGrupa = sefDeGrupa;
    }

    // Gettere si settere
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public List<Student> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<Student> studenti) {
        this.studenti = studenti;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Student getSefDeGrupa() {
        return sefDeGrupa;
    }

    public void setSefDeGrupa(Student sefDeGrupa) {
        this.sefDeGrupa = sefDeGrupa;
    }
}
