package br.gov.al.sefaz.tributario.pdfhandler;

import br.gov.al.sefaz.tributario.pdfhandler.exception.PdfInvalidoException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PdfBeneficiario {

    public static String extraiTextoDoPDF(File caminho) {
        try (PDDocument pdfDocument = PDDocument.load(caminho)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(pdfDocument);
        } catch (IOException e) {
            throw new PdfInvalidoException("Arquivo inválido!");
        }
    }
    public static void validadorPdf(File file) {
        if (!extraiTextoDoPDF(file).contains("TERMO DE QUITAÇÃO"))
            throw new PdfInvalidoException("O Arquivo enviado não é válido");
    }
    public static HashMap<String, String> extrairDados(File file){
        HashMap<String, String> dados = new HashMap<>();
        final String texto = extraiTextoDoPDF(file);

        List<String> lines = texto.lines().collect(Collectors.toList());


        for (String line : lines)
            System.out.println(line);
        for (String line : lines) {

            String[] keyAndValue = line.split(":");
            if (keyAndValue.length == 2)
                dados.put(keyAndValue[0], keyAndValue[1].strip());
        }
        return dados;
    }
}
