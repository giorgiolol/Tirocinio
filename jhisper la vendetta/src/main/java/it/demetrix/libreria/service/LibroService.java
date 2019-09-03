package it.demetrix.libreria.service;

import it.demetrix.libreria.domain.Libro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Libro}.
 */
public interface LibroService {

    /**
     * Save a libro.
     *
     * @param libro the entity to save.
     * @return the persisted entity.
     */
    Libro save(Libro libro);

    Libro update(Libro libro) throws Exception;
    /**
     * Get all the libros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Libro> findAll(Pageable pageable);


    /**
     * Get the "id" libro.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Libro> findOne(Long id);

    /**
     * Delete the "id" libro.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<Libro> findByAutore(Pageable pageable, String autore);
}
