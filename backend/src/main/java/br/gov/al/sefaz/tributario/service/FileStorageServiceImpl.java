package br.gov.al.sefaz.tributario.service;

import br.gov.al.sefaz.tributario.pdfhandler.PDF;
import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final String filenameDi = "di.pdf";
    private final String filenameDmi = "dmi.pdf";

    private final Path root = Paths.get(".pdfs");

    @Override public void init() {
        try {
            Files.createDirectory(root);
            Files.setAttribute(root, "dos:hidden", true);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar o diretorio pra o upload dos pdfs!");
        }
    }

    @Override public void save(MultipartFile receivedFile, PDF.Tipo tipo) {
        final String filename = (tipo == PDF.Tipo.DMI) ? filenameDmi : filenameDi;

        Path filepath = this.root.resolve(filename);
        File pdf = filepath.toFile();

        deleteFileIfExists(pdf);

        tryToCopyFile(receivedFile, filepath);
        validateSavedFile(pdf, tipo);
    }

    private static void deleteFileIfExists(File pdf) {
        if (pdf.exists())
            pdf.delete();
    }

    private static void tryToCopyFile(MultipartFile file, Path newfilePath) {
        try {
            Files.copy(file.getInputStream(), newfilePath);
        }
        catch (Exception e) {
            deleteFileIfExists(newfilePath.toFile());
            throw new RuntimeException("Não foi possível salvar o Arquivo. Erro: " + e.getMessage());
        }
    }

    private static void validateSavedFile(File file, PDF.Tipo tipo) {
        try {
            PDF.from(file, tipo);
        } catch (Exception e) {
            file.delete();
            throw new PdfInvalidoException(tipo, e);
        }
    }

    @Override public PDF loadDi() throws IOException {
        return PDF.di(root.resolve(filenameDi).toFile());
    }
    @Override public PDF loadDmi() throws IOException {
        return PDF.dmi(root.resolve(filenameDmi).toFile());
    }

    @Override public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
}
