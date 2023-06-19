package br.gov.al.sefaz.precatorio.pdfhandler;

import br.gov.al.sefaz.precatorio.exception.PdfInvalidoException;
import br.gov.al.sefaz.precatorio.pdfhandler.util.Searcher;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PdfBeneficiario {
    private final Path arquivo;

    public PdfBeneficiario(Path path) {
        if (!path.toFile().exists())
            throw new PdfInvalidoException("Arquivo inexistente!");

        this.arquivo = path;

        try (PDDocument pdf = PDDocument.load(arquivo.toFile())) {
            validarPdf(pdf);
        } catch (Exception e) {
            throw new PdfInvalidoException("O Arquivo enviado não é válido", e);
        }
    }

    private static void validarPdf(PDDocument pdf) {
        try {
            new Searcher(pdf).doSearch("termo de quitação").getFirstLine().orElseThrow();
        } catch (Exception e) {
            throw new PdfInvalidoException("Formatação do Pdf inválida", e);
        }
    }

    public Map<String, String> extrairDados() {
        try (PDDocument pdf = PDDocument.load(arquivo.toFile())) {
            Map<String, String> dados = getDadosPadrao();

            List<Row> rows = extrairRowsDoPdf(pdf);

            for (Row row : rows) {
                switch (row.campo) {
                    case "MATRICULA": dados.put("matricula", row.valor);
                    case "NOME": dados.put("nome", row.valor.toUpperCase());
                    case "CPF": dados.put("cpf", row.valor);
                    case "VALOR DE FACE BRUTO": dados.put("valFaceBruto", row.valor);
                    case "VALOR DE FACE HONORÁRIOS": dados.put("valFaceHono", row.valor);
                    case "VALOR DE ACORDO BRUTO": dados.put("valAcordoBruto", row.valor);
                    case "VALOR DE ACORDO HONORÁRIOS": dados.put("ValAcordoHono", row.valor);
                    case "VALOR DO AL PREVIDÊNCIA": dados.put("ipaseal", row.valor);
                    case "VALOR DO IRPF": dados.put("irpf", row.valor);
                }
            }

            return dados;
        } catch (Exception e) {
            throw new PdfInvalidoException("O Arquivo enviado não é válido", e);
        }

    }

    private List<Row> extrairRowsDoPdf(PDDocument pdf) throws IOException {
        List<String> lines = new Searcher(pdf).doSearch(":").getLines();

        return lines.stream()
                .map(l -> l.split(":"))
                .map(List::of)
                .map(Row::new)
                .collect(Collectors.toList());
    }

    private static HashMap<String, String> getDadosPadrao() {
        return new HashMap<>() {{
            put("matricula", "");
            put("nome", "");
            put("cpf", "");
            put("valFaceBruto", "0,00");
            put("valFaceHono", "0,00");
            put("valAcordoBruto", "0,00");
            put("ValAcordoHono", "0,00");
            put("ipaseal", "0,00");
            put("irpf", "0,00");
        }};
    }

    static class Row {
        final String campo;
        final String valor;

        Row(String campo, String valor) {
            this.campo = campo.strip().toUpperCase();
            this.valor = valor.strip();
        }

        Row(List<String> list) {
            this(list.get(0), list.get(1));
        }
    }
}
