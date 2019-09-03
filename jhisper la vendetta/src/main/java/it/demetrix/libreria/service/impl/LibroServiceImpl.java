package it.demetrix.libreria.service.impl;

import it.demetrix.libreria.security.users.CustomUser;
import it.demetrix.libreria.service.LibroService;
import it.demetrix.libreria.domain.Libro;
import it.demetrix.libreria.repository.LibroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private JavaMailSender mailSender;

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

    @Override
    @Transactional
    public Libro update(Libro libro) throws Exception{
        if(libroRepository.existsById(libro.getId())) {
            Libro updateLibro = libroRepository.save(libro);
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof CustomUser) {
                CustomUser user = (CustomUser) principal;
                String emailRecipent = user.getEmail();
                SimpleMailMessage email = new SimpleMailMessage();
                email.setTo(emailRecipent);
                email.setSubject("Upadate");
                email.setText("Hai eseguito la seguente modifica" + updateLibro);
                mailSender.send(email);
                return updateLibro;
            }else{
                throw new Exception("Errore");
            }
        }
        return null;
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
