package br.gov.al.sefaz.precatorio.pdfhandler;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PdfBeneficiario {

    protected static String extraiTextoDoPDF(File caminho) {
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

        for (String line : lines) {
            String[] campoValor = line.split(":");

            if (campoValor.length == 2)
                dados.put(campoValor[0].strip(), campoValor[1].strip());
        }
        return dados;
    }

    public static Map<String, String> extrairDadosV2(File file) {
        var tabela = extrairDados(file);
        Map<String, String> dados = new HashMap<>();

        for (var par : mapNomeCampoParaId().entrySet()) {
            String campo = par.getKey();
            String id = par.getValue();

            String valor = tabela.getOrDefault(campo, "");
            dados.put(id, valor);
        }

        return dados;
    }

    private static Map<String, String> mapNomeCampoParaId() {
        return Map.of(           "MATRICULA", "matricula",
                                      "NOME", "nome",
                                       "CPF", "cpf",
                       "VALOR DE FACE BRUTO", "valFaceBruto",
                  "VALOR DE FACE HONORÁRIOS", "valFaceHono",
                     "VALOR DE ACORDO BRUTO", "valAcordoBruto",
                "VALOR DE ACORDO HONORÁRIOS", "ValAcordoHono",
                   "VALOR DO AL PREVIDÊNCIA", "ipaseal",
                             "VALOR DO IRPF", "irpf"
        );
    }
}
