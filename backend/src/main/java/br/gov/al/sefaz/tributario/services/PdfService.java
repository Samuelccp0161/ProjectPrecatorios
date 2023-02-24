package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface PdfService {
    void init();

    void save(MultipartFile file, PDF.Tipo tipo);

    PDF loadDi() throws IOException;

    PDF loadDmi() throws IOException;

    Map<String, String> getDadosParaPreencher() throws IOException;
    void deleteAll();
}
