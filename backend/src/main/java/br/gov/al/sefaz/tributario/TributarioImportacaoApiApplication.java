package br.gov.al.sefaz.tributario;

import br.gov.al.sefaz.tributario.service.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TributarioImportacaoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TributarioImportacaoApiApplication.class, args);
	}

	@Bean
	CommandLineRunner initStorage(FileStorageService storageService) {
		return args -> {
			storageService.deleteAll();
    		storageService.init();
		};
	}
}
