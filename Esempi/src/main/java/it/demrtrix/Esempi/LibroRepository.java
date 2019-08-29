package it.demrtrix.Esempi;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;


public interface LibroRepository extends CrudRepository<Libro,Long> {
    Collection<Libro> findByAutoreOrderByAnno(String autore);
    Collection<Libro> findByAutoreOrderById(String autore);
    Collection<Libro> findByAutore (String autore);


    Collection<Libro> findByAnnoAndAutore(Integer anno, String autore);
}
