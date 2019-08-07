package it.demrtrix.Esempi;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="state")
public class Stato {

    @Id
    @GeneratedValue
    @Column(name="State_id")
    private String id;
    @Column (name="Stato")
    private String stato;

    public Stato (){
    }

    public Stato(String id, String stato) {
        this.id = id;
        this.stato = stato;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return "Stato=" +
                "id='" + id + '\'' +
                ", stato='" + stato + '\'';
    }
}
