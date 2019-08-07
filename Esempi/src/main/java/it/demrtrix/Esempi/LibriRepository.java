package it.demrtrix.Esempi;

import org.springframework.data.repository.CrudRepository;

import javax.persistence.OneToOne;
import java.util.List;


public interface LibriRepository extends CrudRepository<Libri,Long> {
    List<Libri> findByAutoreOrderByAnno(String autore);
    List<Libri> findByAutoreOrderById(String autore);
    List<Libri> findByAnnoAndAutore(Integer anno,String autore);
    //Libri findyById (Long id);
}
