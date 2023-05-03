package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import br.gov.al.sefaz.tributario.pdfhandler.PdfBeneficiario;
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
        dadosDI = PDF.di(getRootDir().resolve(FILENAME_DI).toFile()).getTabela();
    }

    public void saveDmiFile(MultipartFile receivedFile) {
        saveFile(FILENAME_DMI, receivedFile);
        dadosDMI = PDF.dmi(getRootDir().resolve(FILENAME_DMI).toFile()).getTabela();
    }

    @Override
    public void saveFileBeneficiario(MultipartFile receivedFile) {
        saveFile(FILENAME_BENEFICIARIO, receivedFile);
        PdfBeneficiario.validadorPdf(getRootDir().resolve(FILENAME_BENEFICIARIO).toFile());
        dadosBeneficiario = PdfBeneficiario.extrairDados(getRootDir().resolve(FILENAME_BENEFICIARIO).toFile());
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
