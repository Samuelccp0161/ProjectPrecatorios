package br.gov.al.sefaz.tributario.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PdfService {
    String FILENAME_DMI = "dmi.pdf";
    String FILENAME_DI = "di.pdf";
    String FILENAME_BENEFICIARIO = "beneficiario.pdf";

    void init();
    void deleteRoot();
    void saveDiFile(MultipartFile receivedFile);
    void saveDmiFile(MultipartFile receivedFile);
    void saveFileBeneficiario(MultipartFile receivedFile);

    Map<String, String> extrairDadosTributario();

    Map<String, String> extrairDadosBeneficiario();
}
