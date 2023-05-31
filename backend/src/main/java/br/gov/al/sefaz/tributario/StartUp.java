package br.gov.al.sefaz.tributario;

import br.gov.al.sefaz.tributario.service.impl.PdfServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StartUp {

	public static void main(String[] args) {
		SpringApplication.run(StartUp.class, args);
	}

	@Bean
	CommandLineRunner initStorage(PdfServiceImpl storageService) {
		return args -> {
			storageService.deleteRoot();
    		storageService.init();
		};
	}
}
