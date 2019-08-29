package it.demrtrix.Esempi;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

@Service
public class LibroServiceImp implements LibroService {
    @Autowired
    private LibroRepository libroRepository;
    private static final Log log = LogFactory.getLog(EsempiApplication.class);

    @Override
    public Collection<Libro> findByAutore(String autore) {
        log.info(libroRepository.findByAutore(autore));
        return libroRepository.findByAutore(autore) ;
    }

    @Override
    public Libro findById(Long id) {

        Optional<Libro> libroOptional = libroRepository.findById(id);
        if(libroOptional.isPresent()){
            Libro libro = libroOptional.get();
            return libroOptional.get();
        } else{
            throw new NoSuchElementException("Il libro con l'id "+id+" non esiste");
        }
    }

    @Override
    public Collection<Libro> getAllLibri() {
        Collection<Libro> converter = new ArrayList<Libro>();
        Iterable<Libro> queryResponse = libroRepository.findAll();
        for (Libro libro : queryResponse){
            converter.add(libro);
        }
        return converter;
    }

    @Override
    public Libro updateLibro(Long id,Libro libro) {
        if(libro.getAnno()<=0L){
            throw new IllegalArgumentException("L' anno deve essere maggiore di 0 ");
        }
        libro.setTitolo(libro.getTitolo().trim());
        if(libro.getTitolo().length()==0 || libro.getTitolo().length()>40){
            throw new IllegalArgumentException("il tiolo deve rispettare i criteri del database");
        }
        libro.setAutore(libro.getAutore().trim());
        if(libro.getAutore().length()==0 || libro.getAutore().length()>40){
            throw new IllegalArgumentException("l'autore deve rispettare i criteri del database");
        }
        if(id<=0){
            throw new IllegalArgumentException("L' id deve essere maggiore di 0 ");
        }
        if(!libroRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("Il libro con l'id "+id+" non esiste, usare il metodo post");
        }
        libro.setId(id);
        libro= libroRepository.save(libro);
        return libro;

    }

    @Override
    public Libro addLibro(Libro libro){
        if(libro.getAnno()<=0L){
            throw new IllegalArgumentException("L' anno deve essere maggiore di 0 ");
        }
        libro.setTitolo(libro.getTitolo().trim());
        if(libro.getTitolo().length()==0 || libro.getTitolo().length()>40){
            throw new IllegalArgumentException("il tiolo deve rispettare i criteri del database");
        }
       libro.setAutore(libro.getAutore().trim());
        if(libro.getAutore().length()==0 || libro.getAutore().length()>40){
            throw new IllegalArgumentException("l'autore deve rispettare i criteri del database");
        }
        libro = libroRepository.save(libro);
        return libro;

    }

    @Override
    public Libro removeLibro(Long id) {
        if(id<=0L){
            throw new IllegalArgumentException("L'id deve essee maggiore di 0");
        }
        Optional<Libro> libroOptional = libroRepository.findById(id);
        if(libroOptional.isPresent()){
            Libro libro = libroOptional.get();
            libroRepository.delete(libro);
            return libro;
        } else{
            throw new NoSuchElementException("Il libro con id "+id+" non esiste");
        }

    }
}
