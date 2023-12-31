package br.gov.al.sefaz.precatorio.service.impl;

import br.gov.al.sefaz.precatorio.pdfhandler.PdfDI;
import br.gov.al.sefaz.precatorio.pdfhandler.PdfDMI;
import br.gov.al.sefaz.precatorio.service.PdfService;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfServiceImpl implements PdfService {
    private Map<String, String> dadosDI;
    private Map<String, String> dadosDMI;
    private Map<String, String> dadosBeneficiario;

    private final Path root = Paths.get(".pdfs");

    public Path getRootDir() {
        return root;
    }

    public void init() {
        try {
            if (!getRootDir().toFile().exists()) {
                Files.createDirectory(root);
                Files.setAttribute(root, "dos:hidden", true);
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretorio pra o upload dos pdfs!", e);
        }
    }

    public void deleteRoot() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    protected void saveFile(String filename, MultipartFile file) {
        Path path = root.resolve(filename);
        if (path.toFile().exists())
            path.toFile().delete();

        try {
            Files.copy(file.getInputStream(), path);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível salvar o arquivo", e);
        }
    }

    public void saveDiFile(MultipartFile receivedFile) {
        saveFile(FILENAME_DI, receivedFile);
        dadosDI = new PdfDI(getRootDir().resolve(FILENAME_DI)).extrairDados();
    }

    public void saveDmiFile(MultipartFile receivedFile) {
        saveFile(FILENAME_DMI, receivedFile);
        dadosDMI = new PdfDMI(getRootDir().resolve(FILENAME_DMI)).extrairDados();
    }

    @Override
    public void saveFileBeneficiario(MultipartFile receivedFile) {
        saveFile(FILENAME_BENEFICIARIO, receivedFile);
        dadosBeneficiario = new br.gov.al.sefaz.precatorio.pdfhandler.PdfBeneficiario(getRootDir().resolve(FILENAME_BENEFICIARIO)).extrairDados();
    }

    public Map<String, String> extrairDadosTributario() {
        var dados = new HashMap<>(dadosDI);
        dados.putAll(dadosDMI);

        return dados;
    }

    @Override
    public Map<String, String> extrairDadosBeneficiario() {
        return dadosBeneficiario;
    }

    protected Map<String, String> getDadosDI() {
        return dadosDI;
    }

    protected Map<String, String> getDadosDMI() {
        return dadosDMI;
    }
}
