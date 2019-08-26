package it.demrtrix.Esempi;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import java.util.Set;
import java.util.stream.Collectors;


public interface LibroRepository extends CrudRepository<Libro,Long> {
    List<Libro> findByAutoreOrderByAnno(String autore);
    List<Libro> findByAutoreOrderById(String autore);
    List<Libro> findByAutore (String autore);


    List<Libro> findByAnnoAndAutore(Integer anno, String autore);
}
