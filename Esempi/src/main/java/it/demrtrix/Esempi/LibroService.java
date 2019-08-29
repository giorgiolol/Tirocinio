package it.demrtrix.Esempi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

public interface LibroService {


    abstract Collection<Libro> findByAutore (String autore);
    abstract Libro findById (Long id);
    abstract Collection<Libro> getAllLibri ();
    abstract Libro updateLibro (Long id,Libro libro);
    abstract Libro addLibro (Libro libro);
    abstract Libro removeLibro(Long id);
}
