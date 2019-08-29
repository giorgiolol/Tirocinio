package it.demetrix.libreria.service.impl;

import it.demetrix.libreria.service.LibroService;
import it.demetrix.libreria.domain.Libro;
import it.demetrix.libreria.repository.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Libro}.
 */
@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    private final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);

    private final LibroRepository libroRepository;

    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    /**
     * Save a libro.
     *
     * @param libro the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Libro save(Libro libro) {
        log.debug("Request to save Libro : {}", libro);
        return libroRepository.save(libro);
    }

    /**
     * Get all the libros.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Libro> findAll(Pageable pageable) {
        log.debug("Request to get all Libros");
        return libroRepository.findAll(pageable);
    }


    /**
     * Get one libro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Libro> findOne(Long id) {
        log.debug("Request to get Libro : {}", id);
        return libroRepository.findById(id);
    }

    /**
     * Delete the libro by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Libro : {}", id);
        libroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Libro> findByAutore(Pageable pageable, String autore) {
        log.debug("Request to get all Libros");
        return libroRepository.findByAutore(pageable,autore);

    }
}
