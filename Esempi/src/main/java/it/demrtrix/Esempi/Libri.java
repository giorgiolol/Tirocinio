package it.demrtrix.Esempi;

import javax.persistence.*;
@Entity
@Table(name = "Libri")
public class Libri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="titolo")
    private String titolo;
    @Column(name="autore")
    private String autore;
    @Column(name="anno")
    private Integer anno;

    protected Libri(){

    }

    public Libri(String titolo, String autore, Integer anno){
        this.titolo=titolo;
        this.anno=anno;
        this.autore=autore;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        if(this == null){
            return "none";
        }
        return "Libro{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", anno=" + anno +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }
}
