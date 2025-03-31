package dto;

import java.util.List;

public class ProfesorDTO {
    private String nume;
    private List<String> cursuri;
    private List<String> email;
    private String functie;

    // Constructor fara parametrii
    public ProfesorDTO() {

    }

    // Constructor cu parametrii
    public ProfesorDTO(String nume, List<String> cursuri, List<String> email, String functie) {
        this.nume = nume;
        this.cursuri = cursuri;
        this.email = email;
        this.functie = functie;
    }

    // Gettere si settere
    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<String> getCursuri() {
        return cursuri;
    }

    public void setCursuri(List<String> cursuri) {
        this.cursuri = cursuri;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }
}
