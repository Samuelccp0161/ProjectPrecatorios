package br.gov.al.sefaz.tributario;

import br.gov.al.sefaz.tributario.services.PdfService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TributarioImportacaoApiApplication {

	public static void main(String[] args) {
		WebDriverManager.firefoxdriver().setup();

		SpringApplication.run(TributarioImportacaoApiApplication.class, args);
	}

	@Bean
	CommandLineRunner initStorage(PdfService storageService) {
		return args -> {
			storageService.deleteRoot();
    		storageService.init();
		};
	}
}
