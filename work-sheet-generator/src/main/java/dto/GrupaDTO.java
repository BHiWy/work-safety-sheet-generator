package dto;

import java.util.List;

public class GrupaDTO {
    private String cod;
    private List<StudentDTO> studenti;
    private Integer an;
    private StudentDTO sefDeGrupa;

    // Constructor fara argumente
    public GrupaDTO() {}

    // Constructor
    public GrupaDTO(String cod, List<StudentDTO> studenti, Integer an, StudentDTO sefDeGrupa) {
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

    public List<StudentDTO> getStudenti() {
        return studenti;
    }

    public void setStudenti(List<StudentDTO> studenti) {
        this.studenti = studenti;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public StudentDTO getSefDeGrupa() {
        return sefDeGrupa;
    }

    public void setSefDeGrupa(StudentDTO sefDeGrupa) {
        this.sefDeGrupa = sefDeGrupa;
    }
}
