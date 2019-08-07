package it.demrtrix.Esempi;

import javax.persistence.*;

@Entity
@Table(name = "Person")
public class Autore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="firstname")
    private String nome;
    @Column(name="lastname")
    private String cognome;
    @Column(name="state")
    private String stato;
    @Column (name="username")
    private String username;

    public Autore(){

    }

    public Autore(Long id, String nome, String cognome, String stato, String username) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.stato = stato;
        this.username = username;
    }


    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public String toString() {
        return "Autore{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", stato='" + stato + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
