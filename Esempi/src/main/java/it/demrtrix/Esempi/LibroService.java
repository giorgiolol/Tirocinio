package it.demrtrix.Esempi;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface LibroService {


    abstract Collection<Libro> findByAutore (String autore);
    abstract Libro findById (Long id);
    abstract Collection<Libro> getAllLibri ();
    abstract Libro updateLibro (Long id,Libro libro);
    abstract Libro addLibro (Libro libro);
    abstract Libro removeLibro(Long id);

}
