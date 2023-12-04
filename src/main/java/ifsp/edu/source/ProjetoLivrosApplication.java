package ifsp.edu.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ifsp.edu.source")
public class ProjetoLivrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoLivrosApplication.class, args);
	}

}
