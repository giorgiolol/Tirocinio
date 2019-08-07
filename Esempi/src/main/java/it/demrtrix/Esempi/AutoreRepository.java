package it.demrtrix.Esempi;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AutoreRepository extends CrudRepository<Autore,Long> {
    //SList<Autore> findByLastname (String lastname);
}
