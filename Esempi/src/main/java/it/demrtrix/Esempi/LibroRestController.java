package it.demrtrix.Esempi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import javax.persistence.Entity;


@RestController
@RequestMapping("/rest/libri")
public class LibroRestController {

    @Autowired
    private LibriRepository libriRepository;
    private static final Log log = LogFactory.getLog(EsempiApplication.class);


    @RequestMapping(method = RequestMethod.POST)
    public String postId(@RequestParam(name="id", required=false, defaultValue="-1L") Long id) {
        if(id== -1L){
            return "requied specific id";
        }
        Libri libro = libriRepository.findById(id).get();
        if(libro != null){
            log.info(libro);
            return "Post :"+libro;
        }
        return "none";
    }
    @RequestMapping(method = RequestMethod.GET)
    public String getId(@RequestParam(name="id", required=false, defaultValue="-1L") Long id) {
        if(id== -1L){
            return "requied specific id";
        }
        Libri libro = libriRepository.findById(id).get();
        if(libro != null){
            log.info(libro);
            return "Get :"+libro;
        }
        return "none";
    }

    @RequestMapping(value ="/{id}" , method = RequestMethod.GET)
    public String getIdByPath(@PathVariable Long id) {
        if(id== -1L){
            return "requied specific id";
        }
        Libri libro = libriRepository.findById(id).get();
        if(libro != null){
            log.info(libro);
            return "Path :"+libro;
        }
        return "none";
    }





}
