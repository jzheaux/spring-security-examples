package io.jzheaux.pluralsight.instagraph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;

@SpringBootApplication
public class InstagraphApplication {

	public static void main(String[] args) {
		SpringApplication.run(InstagraphApplication.class, args);
	}

}
