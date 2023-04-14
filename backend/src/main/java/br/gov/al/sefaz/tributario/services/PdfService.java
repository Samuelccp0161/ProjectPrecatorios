package br.gov.al.sefaz.tributario.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PdfService {
    void init();
    void deleteRoot();
    void saveDiFile(MultipartFile receivedFile);
    void saveDmiFile(MultipartFile receivedFile);
    Map<String, String> extrairDados();
}
