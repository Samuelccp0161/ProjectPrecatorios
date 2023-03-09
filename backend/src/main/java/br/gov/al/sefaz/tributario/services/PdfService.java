package br.gov.al.sefaz.tributario.services;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class PdfService {
    protected static final String FILENAME_DMI = "dmi.pdf";
    protected static final String FILENAME_DI = "di.pdf";

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

    protected void saveFile(String filename, MultipartFile file) throws IOException {
        Path path = root.resolve(filename);
        if (path.toFile().exists())
            path.toFile().delete();

        Files.copy(file.getInputStream(), path);
    }

    public void saveDiFile(MultipartFile receivedFile) throws IOException {
        saveFile(FILENAME_DI, receivedFile);
        PDF.di(getRootDir().resolve(FILENAME_DI).toFile());
    }

    public Map<String, String> extrairDi() {
        try {
            return PDF.di(getRootDir().resolve(FILENAME_DI).toFile()).getTabela();
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    public void saveDmiFile(MultipartFile receivedFile) throws IOException {
        saveFile(FILENAME_DMI, receivedFile);
        PDF.dmi(getRootDir().resolve(FILENAME_DMI).toFile());
    }

    public Map<String, String> extrairDmi() {
        try {
            return PDF.dmi(getRootDir().resolve(FILENAME_DMI).toFile()).getTabela();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> extrairDados() {
        var dados = extrairDi();
        dados.putAll(extrairDmi());

        return dados;
    }
}
