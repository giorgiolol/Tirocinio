package it.demrtrix.Esempi;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.*;


@RestController
@RequestMapping("/rest/libri")
public class LibroRestController {
    @Autowired
    private LibroServiceImp libroService;
    private static final Log log = LogFactory.getLog(EsempiApplication.class);

    /**
     * Metodo per inserire valori nel database.
     * Risponde a una chimata post sul percorso localhost:7900/rest/libri
     *

     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Libro> addLibro(@RequestBody Libro libro) {
        if(libro.getAnno()<=0L){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        libro.setTitolo(libro.getTitolo().trim());
        if(libro.getTitolo().length()==0 || libro.getTitolo().length()>40){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        libro.setAutore(libro.getAutore().trim());
        if(libro.getAutore().length()==0 || libro.getAutore().length()>40){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

        libro = libroService.addLibro(libro);
        return new ResponseEntity<Libro>(libro, HttpStatus.CREATED);
    }


    /**
     * Metodo che permette l'idirizzamento diretto di un libro tramite id.
     * Per cercare direttamente per id bisognera effettuare la richiesta a localhost:7900/rest/libri/{id}
     * Dove id è l'elemeto a ccui si vuole accedere
     * @param id
     * @return
     */
    @RequestMapping(value ="/{id}")
    public ResponseEntity<Libro> getIdByPath(@PathVariable Long id) {
        if(id<0L){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        Libro libro = libroService.findById(id);
        return new ResponseEntity<Libro>(libro, HttpStatus.OK);
    }

    /**
     * Aggiorna un elemento del database.
     * Bisognera inviare una richiesta put a localhost:7900/rest/libri/{id}
     * Con {id} della risorsa che si vuole modificare e nel body l'oggetto come elemnto del body, come un json
     * @param id
     * @param libro
     * @return
     */
    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    public ResponseEntity<Libro> putLibro(@PathVariable(name="id") Long id,
                                          @RequestBody Libro libro) {
        if(libro.getAnno()<=0L){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        libro.setTitolo(libro.getTitolo().trim());
        if(libro.getTitolo().length()==0 || libro.getTitolo().length()>40){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        libro.setAutore(libro.getAutore().trim());
        if(libro.getAutore().length()==0 || libro.getAutore().length()>40){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        if(id<0L){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        libro = libroService.updateLibro(id,libro);
        return new ResponseEntity<Libro>(libro, HttpStatus.OK);
    }

    /**
     * Elimina un libro.
     * Risponde una chiamata delete a localhost:7900/rest/libri, specificando nella richiesta l'id dell'elemento che si vuole eliminare
     * @param id
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Libro> deleteLibroById(@RequestParam(name="id", defaultValue="0") Long id){
        if(id<0L){
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }
        Libro libro = libroService.removeLibro(id);
        return new ResponseEntity<>(libro,HttpStatus.OK);
    }

    /**
     * F
     * @return
     */
    @RequestMapping()
    public ResponseEntity<JsonNode> getAllLibri () {

        List<Libro> libri = (List<Libro>)   libroService.getAllLibri();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(libri);
        return new ResponseEntity<>( node,HttpStatus.OK);

    }

    @RequestMapping(value ="/autore/{autore}")
    public ResponseEntity<Collection<Libro>> getLibribyAutorebyPath(@PathVariable String autore) {
        if(autore.length()<0 || autore.length()>40 ){
            throw new IllegalArgumentException("L'autore deve rispettare i criteri del database");
        }
        Collection<Libro> libri =libroService.findByAutore(autore);
        return new ResponseEntity<>(libri, HttpStatus.OK);
    }

   @RequestMapping(value ="/autore", method = RequestMethod.GET)
    public ResponseEntity<List<Libro>> getLibribyAutore(@RequestParam(name="autore", required=false, defaultValue="") String autore) {
       if(autore.length()<0 || autore.length()>40 ){
           return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
       }
        List<Libro> libri =(List<Libro>) libroService.findByAutore(autore);
       return new ResponseEntity<>(libri, HttpStatus.OK);
    }


    @RequestMapping(value ="/error")
    public void erore(){
        log.info("\n\n---------------------------EHI ERRORE GROSSO QUI---------------------------------\n\n");
    }
    @ExceptionHandler({SQLException.class})
    public ResponseEntity<?> catchSqlException (){
        return new ResponseEntity<>( HttpStatus.I_AM_A_TEAPOT);
    }
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<?> catchIllegalArgumantException (){
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<?> catchNoSuchElement (){
        return new ResponseEntity<>( HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<?> catchNullPointer (){
        return new ResponseEntity<>( HttpStatus.I_AM_A_TEAPOT);
    }

}
