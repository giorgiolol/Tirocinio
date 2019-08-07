package it.demrtrix.Esempi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableAutoConfiguration
public class EsempiApplication {

	private static final Log log = LogFactory.getLog(EsempiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EsempiApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo (LibriRepository repository){
		return (args) -> {

			log.info("output di tutti i libri");
			for(Libri libri : repository.findAll()){
				log.info(libri );
			}

			repository.findById(1L)
					.ifPresent(Libro -> {
						log.info("Libro trovato con findById(1L)");
						log.info("------------------------------");
						log.info(Libro);
					});

			log.info("Libri trovati con l'autore a");
			log.info("----------------------------");
			repository.findByAutoreOrderByAnno("a").forEach(libro ->{
				log.info(libro);
			});
			log.info("Libri trovati con l'autore a");
			log.info("----------------------------");
			repository.findByAutoreOrderById("a").forEach(libro ->{
				log.info(libro);
			});
			log.info("Libri trovati con l'autore a e anno 2");
			log.info("----------------------------");
			repository.findByAnnoAndAutore(2,"a").forEach(libro ->{
				log.info(libro);
			});
		};
	}


}
