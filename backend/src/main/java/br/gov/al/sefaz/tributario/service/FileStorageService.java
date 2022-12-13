package br.gov.al.sefaz.tributario.service;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    void init();

    void save(MultipartFile file, PDF.Tipo tipo);

    PDF loadDi() throws IOException;

    PDF loadDmi() throws IOException;

    void deleteAll();
}
