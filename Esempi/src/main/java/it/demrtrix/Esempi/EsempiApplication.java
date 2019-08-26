package it.demrtrix.Esempi;
import  it.demrtrix.Esempi.configuration.ApplicationProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties({ApplicationProperties.class})
public class EsempiApplication {

	private static final Log log = LogFactory.getLog(EsempiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EsempiApplication.class, args);
	}




}
